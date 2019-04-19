package meat;

import meat.vm.MeatList;
import meat.vm.MeatObject;

public class Utility {

	public static MeatList asList(MeatObject... elements) {
		return new MeatList(elements);
	}

}
