package com.ecar.domain;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.ecar.domain.exception.InsufficientCreditsException;
import com.ecar.domain.exception.UserAlreadyHasCarException;
import com.ecar.domain.exception.UserHasNoCarException;

/**
 * 
 * Domain class that represents the User and deals with their creation, balance
 * manipulation, email manipulation, etc.
 * 
 *
 */
public class User {

	private String email;
	private AtomicInteger balance;
	private AtomicBoolean hasCar = new AtomicBoolean(false);

	public User(String email, int initialBalance) {
		this.email = email;
		balance = new AtomicInteger(initialBalance);
	}

	public synchronized void decrementBalance() throws InsufficientCreditsException {
		if (balance.get() > 0) {
			balance.decrementAndGet();
		} else {
			throw new InsufficientCreditsException();
		}
	}

	public synchronized void incrementBalance(int amount) {
		balance.getAndAdd(amount);
	}

	public String getEmail() {
		return email;
	}

	public boolean getHasCar() {
		return hasCar.get();
	}

	public int getCredit() {
		return balance.get();
	}

	public synchronized void validateCanRent() throws InsufficientCreditsException, UserAlreadyHasCarException {
		if (getHasCar()) {
			throw new UserAlreadyHasCarException();
		}
		if (getCredit() <= 0) {
			throw new InsufficientCreditsException();
		}

	}

	public synchronized void validateCanReturn() throws UserHasNoCarException {
		if (!getHasCar()) {
			throw new UserHasNoCarException();
		}
	}

	public synchronized void effectiveRent() throws InsufficientCreditsException {
		decrementBalance();
		hasCar.set(true);
	}

	public synchronized void effectiveReturn(int prize) throws UserHasNoCarException {
		if (!getHasCar()) {
			throw new UserHasNoCarException();
		}
		hasCar.set(false);
		incrementBalance(prize);
	}

}
