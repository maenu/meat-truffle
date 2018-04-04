package meat;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.graalvm.collections.Pair;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.Var;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

import meat.node.BlockNode;
import meat.node.BooleanNode;
import meat.node.DictionaryNode;
import meat.node.ListNode;
import meat.node.MeatNode;
import meat.node.MessageSendNode;
import meat.node.NumberNode;
import meat.node.ProgramNode;
import meat.node.StringNode;
import meat.node.VariableNode;

/**
 * Indentation is at the top of the stack.
 */
@BuildParseTree
public class Parser extends BaseParser<Object> {

	public static ProgramNode parse(Language language, Source source) {
		String s;
		try {
			s = IOUtils.toString(source.getInputStream(), Charset.defaultCharset());
		} catch (IOException exception) {
			throw new RuntimeException("Could not read source", exception);
		}
		Parser parser = Parboiled.createParser(Parser.class, language, source);
		ParsingResult<Object> result = new ReportingParseRunner<>(parser.Program()).run(s);
		if (!result.parseErrors.isEmpty()) {
			throw new RuntimeException(ErrorUtils.printParseError(result.parseErrors.get(0)));
		}
		List<MeatNode> statements = (List<MeatNode>) result.resultValue;
		// FIXME can it be null?
		return new ProgramNode(language, null, source.createSection(0, s.length()),
				statements.toArray(new MeatNode[0]));
	}

	private Language language;
	private Source source;

	public Parser(Language language, Source source) {
		super();
		this.language = language;
		this.source = source;
	}

	Rule Program() {
		return Sequence(push(0), Statements(), drop(), EOI);
	}

	Rule Statements() {
		Var<List<MeatNode>> statements = new Var<>(new ArrayList<>());
		return Sequence(OneOrMore(Sequence(Statement(), statements.get().add((MeatNode) pop(1)))),
				push(1, statements.get()));
	}

	Rule Statement() {
		return Sequence(CheckIndentation(), FirstOf(MessageSend(), Literal()), '\n');
	}

	Rule MessageSend() {
		Var<MeatNode> receiver = new Var<>();
		Var<Object[]> message = new Var<>();
		return Sequence(Expression(), receiver.set((MeatNode) pop(1)), Message(), message.set((Object[]) pop(1)),
				ACTION(newMessageSend(receiver.get(), (String) message.get()[0], (List<MeatNode>) message.get()[1])));
	}

	boolean newMessageSend(MeatNode receiver, String selector, List<MeatNode> arguments) {
		return push(1, new MessageSendNode(language, null, newSourceSection(), receiver, selector,
				arguments.toArray(new MeatNode[0])));
	}

	Rule Expression() {
		return FirstOf(Literal(), Sequence('(', MessageSend(), ')'));
	}

	Rule Message() {
		return FirstOf(KeywordMessage(), BinaryMessage(), UnaryMessage());
	}

	Rule KeywordMessage() {
		Var<List<String>> selector = new Var<>(new ArrayList<>());
		Var<List<MeatNode>> arguments = new Var<>(new ArrayList<>());
		return Sequence(
				OneOrMore(Sequence(' ', Identifier(), ": ", selector.get().add(((String) pop(1)) + ":"), Expression(),
						arguments.get().add((MeatNode) pop(1)))),
				ACTION(newKeywordMessage(String.join("", selector.get()), arguments.get())));
	}

	boolean newKeywordMessage(String selector, List<MeatNode> arguments) {
		return push(1, new Object[] { selector, arguments });
	}

	Rule BinaryMessage() {
		Var<String> selector = new Var<>();
		Var<MeatNode> argument = new Var<>();
		return Sequence(' ', Sequence(OneOrMore(AnyOf("+-*/=<>:.")), selector.set(match())), ' ', Expression(),
				argument.set((MeatNode) pop(1)), ACTION(newBinaryMessage(selector.get(), argument.get())));
	}

	boolean newBinaryMessage(String selector, MeatNode argument) {
		return push(1, new Object[] { selector, Collections.singletonList(argument) });
	}

	Rule UnaryMessage() {
		Var<String> selector = new Var<>();
		return Sequence(' ', Identifier(), selector.set((String) pop(1)), ACTION(newUnaryMessage(selector.get())));
	}

	boolean newUnaryMessage(String selector) {
		return push(1, new Object[] { selector, Collections.emptyList() });
	}

	Rule Literal() {
		return FirstOf(BooleanLiteral(), NumberLiteral(), VariableLiteral(), StringLiteral(), BlockLiteral(),
				DictionaryLiteral(), ListLiteral());
	}

	Rule BooleanLiteral() {
		Var<Boolean> value = new Var<>();
		return Sequence(FirstOf("true", "false"), value.set(Boolean.valueOf(match())),
				ACTION(newBooleanLiteral(value.get())));
	}

	boolean newBooleanLiteral(boolean value) {
		return push(1, new BooleanNode(language, null, newSourceSection(), value));
	}

	Rule StringLiteral() {
		return Sequence(FirstOf(StringSingleLine(), StringMultiLine()), ACTION(newStringLiteral((String) pop(1))));
	}

	boolean newStringLiteral(String value) {
		return push(1, new StringNode(language, null, newSourceSection(), value));
	}

	Rule StringSingleLine() {
		return Sequence("'", Sequence(ZeroOrMore(FirstOf("''", NoneOf("'"))), push(1, match().replaceAll("''", "'"))),
				"'");
	}

	Rule StringMultiLine() {
		Var<List<String>> lines = new Var<>(new ArrayList<>());
		return Sequence("\"\n", push(((Integer) peek()) + 1),
				OneOrMore(Sequence(StringMultiLineLine(), lines.get().add((String) pop(1)))), drop(),
				CheckIndentation(), "\"", push(1, String.join("\n", lines.get())));
	}

	Rule StringMultiLineLine() {
		return Sequence(CheckIndentation(), Sequence(ZeroOrMore(NoneOf("\n")), push(1, match())), '\n');
	}

	Rule NumberLiteral() {
		Var<BigDecimal> value = new Var<>();
		return Sequence(
				Sequence(FirstOf('0', Sequence(CharRange('1', '9'), ZeroOrMore(CharRange('0', '9')))),
						Optional(Sequence('.', OneOrMore(CharRange('0', '9'))))),
				value.set(new BigDecimal(match())), ACTION(newNumberLiteral(value.get())));
	}

	boolean newNumberLiteral(BigDecimal value) {
		return push(1, new NumberNode(language, null, newSourceSection(), value));
	}

	Rule ListLiteral() {
		Var<List<MeatNode>> statements = new Var<>(Collections.emptyList());
		return Sequence('{', push(((Integer) peek()) + 1),
				Optional(Sequence('\n', Statements(), statements.set((List<MeatNode>) pop(1)))), drop(),
				CheckIndentation(), '}', ACTION(newListLiteral(statements.get())));
	}

	boolean newListLiteral(List<MeatNode> statements) {
		return push(1, new ListNode(language, null, newSourceSection(), statements.toArray(new MeatNode[0])));
	}

	Rule DictionaryLiteral() {
		Var<List<Pair<MeatNode, MeatNode>>> entries = new Var<>(new ArrayList<>());
		return Sequence('<', push(((Integer) peek()) + 1), Optional(Sequence('\n',
				OneOrMore(Sequence(DictionaryLiteralEntry(), entries.get().add((Pair<MeatNode, MeatNode>) pop(1)))))),
				drop(), CheckIndentation(), '>', ACTION(newDictionaryLiteral(entries.get())));
	}

	boolean newDictionaryLiteral(List<Pair<MeatNode, MeatNode>> entries) {
		return push(1, new DictionaryNode(language, null, newSourceSection(), entries.toArray(new Pair[0])));
	}

	Rule DictionaryLiteralEntry() {
		Var<MeatNode> key = new Var<>();
		Var<MeatNode> value = new Var<>();
		return Sequence(Statement(), key.set((MeatNode) pop(1)), push(((Integer) peek()) + 1), Statement(),
				value.set((MeatNode) pop(1)), drop(), push(1, Pair.create(key.get(), value.get())));
	}

	Rule VariableLiteral() {
		Var<String> name = new Var<>();
		return Sequence(Identifier(), name.set((String) pop(1)), ACTION(newVariableLiteral(name.get())));
	}

	boolean newVariableLiteral(String name) {
		return push(1, new VariableNode(language, null, newSourceSection(), name));
	}

	Rule BlockLiteral() {
		Var<List<String>> parameters = new Var<>();
		Var<List<MeatNode>> statements = new Var<>(Collections.emptyList());
		return Sequence('[', Parameters(), parameters.set((List<String>) pop(1)), push(((Integer) peek()) + 1),
				Optional(Sequence('\n', Statements(), statements.set((List<MeatNode>) pop(1)))), drop(),
				CheckIndentation(), ']', ACTION(newBlockLiteral(parameters.get(), statements.get())));
	}

	boolean newBlockLiteral(List<String> parameters, List<MeatNode> statements) {
		return push(1, new BlockNode(language, null, newSourceSection(), parameters.toArray(new String[0]),
				statements.toArray(new MeatNode[0])));
	}

	Rule Parameters() {
		Var<List<String>> parameters = new Var<>(new ArrayList<>());
		return Sequence(ZeroOrMore(Sequence(' ', Identifier(), parameters.get().add((String) pop(1)))),
				push(1, parameters.get()));
	}

	Rule Identifier() {
		return Sequence(
				Sequence(TestNot("vegetable"), FirstOf(CharRange('a', 'z'), CharRange('A', 'Z')),
						ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9')))),
				push(1, match()));
	}

	Rule CheckIndentation() {
		return Sequence(ZeroOrMore('\t'), ACTION(checkIndentation(match().length())));
	}

	boolean checkIndentation(int i) {
		return i == (Integer) peek();
	}

	SourceSection newSourceSection() {
		return source.createSection(matchStart(), matchLength());
	}

}
