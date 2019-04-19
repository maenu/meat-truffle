package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatList;
import meat.vm.MeatObject;

public class ListNode extends MeatNode {

	@Children
	private final MeatNode[] statements;

	public ListNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			MeatNode[] statements) {
		super(language, frameDescriptor, sourceSection);
		this.statements = statements;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		MeatObject[] values = new MeatObject[this.statements.length];
		for (int i = 0; i < this.statements.length; i++) {
			MeatNode statement = this.statements[i];
			values[i] = statement.execute(frame, context, arguments);
		}
		return new MeatList(values);
	}

}
