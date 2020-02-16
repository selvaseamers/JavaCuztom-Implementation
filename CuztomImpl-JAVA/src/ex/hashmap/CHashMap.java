package ex.hashmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class CHashMap<K, V> implements CMap<K, V> {

	private class Node<K, V> {

		K key;
		V value;

		Node(K key, V value, int hashVal) {
			this.key = key;
			this.value = value;
		}

	}

	private ArrayList<Node<K, V>>[] node;

	private int INITIAL_CAPACITIY = 3;
	private int LOAD_FACTOR = 50; // percentage

	private int capacity = 0;

	CHashMap() {
		node = new ArrayList[this.INITIAL_CAPACITIY + 1];
	}

	CHashMap(int INITIAL_CAPACITIY) {
		this.INITIAL_CAPACITIY = INITIAL_CAPACITIY;
		node = new ArrayList[this.INITIAL_CAPACITIY + 1];
	}

	CHashMap(int INITIAL_CAPACITIY, int LOAD_FACTOR) {
		this.INITIAL_CAPACITIY = INITIAL_CAPACITIY;
		if (1 > LOAD_FACTOR || 100 < LOAD_FACTOR) {
			throw new IllegalArgumentException("Load factor should be in between 1 - 100");
		}

		this.LOAD_FACTOR = LOAD_FACTOR;
		node = new ArrayList[this.INITIAL_CAPACITIY + 1];

	}

	@Override
	public V get(K key) {

		int hashVal = generateHashVal(key);

		return null == node[hashVal] ? null
				: null == key ? node[hashVal].get(0).value
						: node[hashVal].stream().filter((nd) -> key.equals(nd.key)).map((nd) -> nd.value).findFirst()
								.orElse(null);

	}

	@Override
	public V put(K key, V value) {

		int hashVal = generateHashVal(key);

		V existingVal = null;

		if (null == key) {
			return handleNullKey(hashVal, key, value);
		}

		if (null != node[hashVal]) {

			existingVal = get(key);

			Node<K, V> existingNode = node[hashVal].stream().filter((array) -> null != array)
					.filter((nd) -> nd.key.equals(key)).findFirst().orElse(null);

			node[hashVal].add(new Node<>(key, value, hashVal));
			capacity++;

			if (null != existingNode) {
				node[hashVal].remove(existingNode);
				capacity--;
			}

		} else {
			node[hashVal] = new ArrayList<Node<K, V>>(Arrays.asList(new Node<>(key, value, hashVal)));
			capacity++;
		}

		System.out.println("Index :: " + hashVal + ", Map Total Capacity :: " + INITIAL_CAPACITIY
				+ ", Map Current Capacity : " + capacity);
		chekLoadFactorAndRehash();

		return existingVal;
	}

	@Override
	public V remove(K key) {
		int hashVal = generateHashVal(key);

		V val = get(key);

		if (null == key) {
			node[hashVal] = null;
			return val;
		}

		Node<K, V> temp = node[hashVal].stream().filter((array) -> null != array).filter((nd) -> key.equals(nd.key))
				.findFirst().orElse(null);

		node[hashVal].remove(temp);
		capacity--;

		return val;
	}

	private int generateHashVal(K key) {
		if (null == key)
			return INITIAL_CAPACITIY;

		int hash = key.hashCode() % (INITIAL_CAPACITIY - 1);

		return hash < 0 ? -hash : hash;
	}

	private V handleNullKey(int hashVal, K key, V value) {

		V existingKey = null;
		if (null != node[hashVal]) {
			existingKey = node[hashVal].get(0).value;
			capacity--;
		}
		node[hashVal] = new ArrayList<Node<K, V>>(Arrays.asList(new Node<>(key, value, hashVal)));
		capacity++;

		return existingKey;

	}

	private void chekLoadFactorAndRehash() {

		float val = (float) capacity / INITIAL_CAPACITIY;
		int CURRENT_CAPACITY_PERCENT = (int) (val * 100);
		System.out.println("==========> Percentage for Load Factor : " + CURRENT_CAPACITY_PERCENT + " %");

		if (CURRENT_CAPACITY_PERCENT >= LOAD_FACTOR) {

			System.out.println("Rehashing ...............Started");

			INITIAL_CAPACITIY *= 2;
			ArrayList<Node<K, V>>[] temp = node;

			node = new ArrayList[INITIAL_CAPACITIY + 1];

			capacity = 0;
			Stream.of(temp).filter((array) -> null != array).forEach((arrays) -> {
				arrays.stream().forEach((nd) -> {
					put(nd.key, nd.value);
				});
			});
			System.out.println("Rehashing ...............Completed ");

		}

	}

	@Override
	public String toString() {
		System.out.print(" { ");
		Stream.of(node).filter((array) -> null != array).forEach((nd) -> {
			nd.stream().forEach((data) -> {
				System.out.print(" [ " + data.key + " : " + data.value + " ]");
			});
		});
		System.out.print(" } ");

		return "";
	}

}
