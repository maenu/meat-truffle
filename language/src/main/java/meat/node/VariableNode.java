package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatList;
import meat.vm.MeatObject;
import meat.vm.MeatString;

public class VariableNode extends MeatNode {

	private final String name;

	public VariableNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection, String name) {
		super(language, frameDescriptor, sourceSection);
		this.name = name;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		MeatList arguments_ = new MeatList(new MeatObject[] { new MeatString(this.name) });
		return context.respondTo("at:", arguments_, context);
	}

}
