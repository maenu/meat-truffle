package meat.node;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatObject;

public abstract class MeatNode extends Node {

	private final SourceSection sourceSection;

	protected MeatNode(SourceSection sourceSection) {
		super();
		this.sourceSection = sourceSection;
	}

	@Override
	public SourceSection getSourceSection() {
		return this.sourceSection;
	}

	public abstract MeatObject execute(MeatObject context);

}
