package meat.node;

import java.util.stream.Stream;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatBlock;
import meat.vm.MeatObject;

public class BlockNode extends MeatNode {

	private final String[] parameters;
	@Children
	private final MeatNode[] statements;

	public BlockNode(SourceSection sourceSection, String[] parameters, MeatNode[] statements) {
		super(sourceSection);
		this.parameters = parameters;
		this.statements = statements;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		if (this.statements.length == 0) {
			return MeatBlock.empty();
		}
		return new MeatBlock(this.parameters, (arguments, context_) -> {
			return Stream.of(this.statements).sequential().map(s -> s.execute(context_)).reduce((a, b) -> b).get();
		});
	}

}
