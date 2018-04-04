package meat.node;

import java.util.stream.Stream;

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
		MeatObject[] values = Stream.of(this.statements).sequential().map(s -> s.execute(frame, context, arguments))
				.toArray(MeatObject[]::new);
		return new MeatList(values);
	}

}
