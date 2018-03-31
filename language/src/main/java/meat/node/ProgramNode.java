package meat.node;

import java.util.Optional;
import java.util.stream.Stream;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatContext;
import meat.vm.MeatObject;

public class ProgramNode extends RootNode {

	private final SourceSection sourceSection;
	@Children
	private final MeatNode[] statements;

	public ProgramNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			MeatNode[] statements) {
		super(language, frameDescriptor);
		assert (statements.length > 0) : "program must contain statements";
		this.sourceSection = sourceSection;
		this.statements = statements;
	}

	@Override
	public SourceSection getSourceSection() {
		return this.sourceSection;
	}

	@Override
	public MeatObject execute(VirtualFrame frame) {
		MeatContext context = new MeatContext(Optional.empty(), false);
		return Stream.of(this.statements).sequential().map(s -> s.execute(context)).reduce((a, b) -> b).get();
	}

}
