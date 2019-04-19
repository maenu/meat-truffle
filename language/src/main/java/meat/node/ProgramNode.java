package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatObject;

public class ProgramNode extends MeatNode {

	@Children
	private final MeatNode[] statements;

	public ProgramNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			MeatNode[] statements) {
		super(language, frameDescriptor, sourceSection);
		assert (statements.length > 0) : "program must contain statements";
		this.statements = statements;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		MeatObject value = null;
		for (int i = 0; i < this.statements.length; i++) {
			MeatNode statement = this.statements[i];
			value = statement.execute(frame);
		}
		return value;
	}

}
