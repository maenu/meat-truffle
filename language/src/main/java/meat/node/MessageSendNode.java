package meat.node;

import java.util.stream.Stream;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatList;
import meat.vm.MeatObject;

public class MessageSendNode extends MeatNode {

	@Child
	private MeatNode receiver;
	private final String selector;
	@Children
	private final MeatNode[] parameters;

	public MessageSendNode(SourceSection sourceSection, MeatNode receiver, String selector, MeatNode[] parameters) {
		super(sourceSection);
		this.receiver = receiver;
		this.selector = selector;
		this.parameters = parameters;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		MeatList arguments = new MeatList(
				Stream.of(this.parameters).map(p -> p.execute(context)).toArray(MeatObject[]::new));
		return this.receiver.execute(context).respondTo(this.selector, arguments, context);
	}

}
