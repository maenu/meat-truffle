package meat.node;

import java.util.stream.Stream;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatBlock;
import meat.vm.MeatObject;

public class BlockNode extends MeatNode {

	private final String[] parameters;
	@Children
	private final MeatNode[] statements;

	public BlockNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			String[] parameters, MeatNode[] statements) {
		super(language, frameDescriptor, sourceSection);
		this.parameters = parameters;
		this.statements = statements;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		// FIXME use arguments
		if (this.statements.length == 0) {
			return MeatBlock.empty();
		}
		return new MeatBlock(this.parameters, (arguments_, context_) -> {
			return Stream.of(this.statements).sequential().map(s -> s.execute(frame, context_, arguments))
					.reduce((a, b) -> b).get();
		});
	}

}
