package meat.node;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatList;
import meat.vm.MeatObject;
import meat.vm.MeatString;

public class VariableNode extends MeatNode {

	private final String name;

	public VariableNode(SourceSection sourceSection, String name) {
		super(sourceSection);
		this.name = name;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		MeatList arguments = new MeatList(new MeatObject[] { new MeatString(this.name) });
		return context.respondTo("at:", arguments, context);
	}

}
