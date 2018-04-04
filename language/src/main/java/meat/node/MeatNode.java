package meat.node;

import java.util.HashMap;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatContext;
import meat.vm.MeatObject;
import meat.vm.MeatVariable;

public abstract class MeatNode extends RootNode {

	protected final SourceSection sourceSection;

	public MeatNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection) {
		super(language, frameDescriptor);
		this.sourceSection = sourceSection;
	}

	@Override
	public SourceSection getSourceSection() {
		return this.sourceSection;
	}

	@Override
	public MeatObject execute(VirtualFrame frame) {
		try {
			// FIXME use own frame descriptor
			FrameDescriptor descriptor = frame.getFrameDescriptor();
			FrameSlot slot = descriptor.findOrAddFrameSlot(MeatContext.FRAME_SLOT, FrameSlotKind.Object);
			Object context = frame.getObject(slot);
			if (context == null) {
				context = new MeatVariable(new MeatContext(new HashMap<>()));
				frame.setObject(slot, context);
			}
			return this.execute(frame, (MeatObject) context, new MeatObject[0] /* FIXME use frame.getArguments() */);
		} catch (FrameSlotTypeException exception) {
			// FIXME what to do?
			throw new RuntimeException(exception);
		}
	}

	public abstract MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments);

}
