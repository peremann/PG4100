package examples;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.sqrt;

/**
 * 
 * http://www.ibm.com/developerworks/java/library/j-ft1/
 *
 */
public class Classifier {
	private Set<Integer> _factors;
	private int _number;

	public Classifier(int number) throws Exception {
		if (number < 1)
			throw new Exception("Can't classify negative numbers");
		_number = number;
		_factors = new HashSet<Integer>();
		_factors.add(1);
		_factors.add(_number);
	}

	private boolean isFactor(int factor) {
		return _number % factor == 0;
	}

	public Set<Integer> getFactors() {
		return _factors;
	}

	private void calculateFactors() {
		for (int i = 1; i <= sqrt(_number) + 1; i++)
			if (isFactor(i))

				addFactor(i);
	}

	private void addFactor(int factor) {
		_factors.add(factor);
		_factors.add(_number / factor);
	}

	private int sumOfFactors() {
		calculateFactors();
		int sum = 0;
		for (int i : _factors)
			sum += i;
		return sum;
	}

	public boolean isPerfect() {
		return sumOfFactors() - _number == _number;
	}

	public boolean isAbundant() {
		return sumOfFactors() - _number > _number;
	}

	public boolean isDeficient() {
		return sumOfFactors() - _number < _number;
	}

	public static boolean isPerfect(int number) throws Exception {
		return new Classifier(number).isPerfect();
	}
}
