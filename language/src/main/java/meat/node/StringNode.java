package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatObject;
import meat.vm.MeatString;

public class StringNode extends MeatNode {

	private final String value;

	public StringNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection, String value) {
		super(language, frameDescriptor, sourceSection);
		this.value = value;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		return new MeatString(this.value);
	}

}
