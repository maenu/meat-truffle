package meat;

import java.io.IOException;
import java.util.stream.IntStream;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public class Main {

	private static final String PROGRAM = String.join("\n",
	// @formatter:off
		"\"",
		"	This is a fibonacci program.",
		"\"",
		"f object: [ i",
		"	(i <= 1) ifTrue: [",
		"		1",
		"	] ifFalse: [",
		"		(f evaluateWith: {",
		"			i - 2",
		"		} in: (<",
		"			'f'",
		"				f",
		"		> asContext)) + (f evaluateWith: {",
		"			i - 1",
		"		} in: (<",
		"			'f'",
		"				f",
		"		> asContext))",
		"	]",
		"]",
		"f evaluateWith: {",
		"	25",
		"} in: context",
		""
	// @formatter:on
	);

	public static void main(String[] args) throws IOException {
		Source source = Source.newBuilder(Language.ID, PROGRAM, "program").build();
		try (Context context = Context.newBuilder(Language.ID).in(System.in).out(System.out).build();) {
			IntStream.range(0, 10).forEach(i -> {
				long before = System.currentTimeMillis();
				Value result = context.eval(source);
				long after = System.currentTimeMillis();
				System.out.println(String.format("meat-truffle,fibonacci-25,%d,%d,%s", i, after - before, result));
			});
		}
		IntStream.range(0, 10).forEach(i -> {
			long before = System.currentTimeMillis();
			int result = f(25);
			long after = System.currentTimeMillis();
			System.out.println(String.format("java,fibonacci-25,%d,%d,%s", i, after - before, result));
		});
	}

	private static int f(int i) {
		return (i <= 1) ? 1 : f(i - 2) + f(i - 1);
	}

}
