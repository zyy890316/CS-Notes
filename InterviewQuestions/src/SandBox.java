import java.util.ArrayDeque;
import java.util.Deque;

public class SandBox {
	public static void main(String[] args) {
		compondInterest();
		System.out.println("```````````````Interst Only Scenrio```````````````");
		compondWithInterstOnly();
	}

	public static void compondInterest() {
		double INTEREST = 8.0;
		int MONTHS = 36;
		double START_CASH_FLOW = 5000.0;
		double currCashFlow = START_CASH_FLOW;
		Deque<Double> cashQueue = new ArrayDeque<>();
		for (int i = 1; i <= 120; i++) {
			double monthlyPayment = getMonthlyPayment(currCashFlow, INTEREST, MONTHS);
			cashQueue.add(monthlyPayment);
			while (cashQueue.size() > MONTHS) {
				cashQueue.pollFirst();
			}
			double sum = 0.0;
			for (double c : cashQueue) {
				sum += c;
			}
			currCashFlow = sum + START_CASH_FLOW;
			// what if we stop putting in cash after x months?
			if (i > 48) {
				currCashFlow = sum;
				// also remove some money per month?
				currCashFlow -= 0;
			}
			System.out.println("current Month: " + i + " current cash Flow: " + currCashFlow);
		}
	}

	public static double getMonthlyPayment(double mortgage, double interest, int months) {
		double monthlyInterest = interest / 100 / 12;
		double mathPower = Math.pow(1 + monthlyInterest, months);
		return mortgage * (monthlyInterest * mathPower / (mathPower - 1));
	}

	public static void compondWithInterstOnly() {
		double INTEREST = 8.0;
		double START_CASH_FLOW = 5000.0;
		double currCashFlow = START_CASH_FLOW;
		Deque<Double> cashQueue = new ArrayDeque<>();
		for (int i = 1; i <= 120; i++) {
			double monthlyPayment = getMonthlyInterestOnlyPayment(currCashFlow, INTEREST);
			cashQueue.add(monthlyPayment);
			double sum = 0.0;
			for (double c : cashQueue) {
				sum += c;
			}
			currCashFlow = sum + START_CASH_FLOW;
			System.out.println("current Month: " + i + " current cash Flow: " + currCashFlow);
		}
	}

	public static double getMonthlyInterestOnlyPayment(double mortgage, double interest) {
		double monthlyInterest = mortgage / 100 / 12 * interest;
		return monthlyInterest;
	}
}
