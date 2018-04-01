package meat.node;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.graalvm.collections.Pair;

import com.oracle.truffle.api.source.SourceSection;

import meat.vm.MeatDictionary;
import meat.vm.MeatObject;

public class DictionaryNode extends MeatNode {

	// FIXME @Children
	private final Pair<MeatNode, MeatNode>[] entries;

	public DictionaryNode(SourceSection sourceSection, Pair<MeatNode, MeatNode>[] entries) {
		super(sourceSection);
		this.entries = entries;
	}

	@Override
	public MeatObject execute(MeatObject context) {
		Map<Object, MeatObject> values = Stream.of(this.entries).sequential().collect(
				Collectors.toMap(p -> p.getLeft().execute(context).value(), p -> p.getRight().execute(context)));
		return new MeatDictionary(values);
	}

}
