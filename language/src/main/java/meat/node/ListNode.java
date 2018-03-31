package meat.node;

import java.util.stream.Stream;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatList;
import meat.vm.MeatObject;

public class ListNode extends MeatNode {

	@Children
	private final MeatNode[] statements;

	public ListNode(SourceSection sourceSection, MeatNode[] statements) {
		super(sourceSection);
		this.statements = statements;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		MeatObject[] values = Stream.of(this.statements).sequential().map(s -> s.execute(context))
				.toArray(MeatObject[]::new);
		return new MeatList(values);
	}

}
