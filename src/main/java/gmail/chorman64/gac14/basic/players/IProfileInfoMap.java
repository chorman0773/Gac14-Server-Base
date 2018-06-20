package gmail.chorman64.gac14.basic.players;

import java.util.Map;

public interface IProfileInfoMap<V> extends IProfileInfoBase<Map<String, IProfileInfoBase<V>>> {
	V get(String key);
	V set(String key,V value);
	IProfileInfoBase<V> getInfo(String key);
}
