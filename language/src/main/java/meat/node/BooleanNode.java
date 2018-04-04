package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatBoolean;
import meat.vm.MeatObject;

public class BooleanNode extends MeatNode {

	private final Boolean value;

	public BooleanNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection, Boolean value) {
		super(language, frameDescriptor, sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		return new MeatBoolean(this.value);
	}

}
