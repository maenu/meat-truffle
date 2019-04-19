package meat.node;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;

import meat.Language;
import meat.vm.MeatList;
import meat.vm.MeatObject;

public class MessageSendNode extends MeatNode {

	@Child
	private MeatNode receiver;
	private final String selector;
	@Children
	private final MeatNode[] parameters;

	public MessageSendNode(Language language, FrameDescriptor frameDescriptor, SourceSection sourceSection,
			MeatNode receiver, String selector, MeatNode[] parameters) {
		super(language, frameDescriptor, sourceSection);
		this.receiver = receiver;
		this.selector = selector;
		this.parameters = parameters;
	}

	@Override
	public MeatObject execute(VirtualFrame frame, MeatObject context, MeatObject[] arguments) {
		// FIXME new frame
		MeatObject[] arguments_ = new MeatObject[this.parameters.length];
		for (int i = 0; i < this.parameters.length; i++) {
			MeatNode parameter = this.parameters[i];
			MeatObject argument = parameter.execute(frame, context, arguments);
			arguments_[i] = argument;
		}
		return this.receiver.execute(frame, context, arguments).respondTo(this.selector, new MeatList(arguments_),
				context);
	}

}
