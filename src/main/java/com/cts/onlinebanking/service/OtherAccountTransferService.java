package com.cts.onlinebanking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.onlinebanking.model.CurrentAccount;
import com.cts.onlinebanking.model.SavingsAccount;
import com.cts.onlinebanking.model.TransferOthersAccount;
import com.cts.onlinebanking.repository.CurrentAccountRepository;
import com.cts.onlinebanking.repository.SavingsAccountRepository;
import com.cts.onlinebanking.repository.TransferRepository;

@Service
public class OtherAccountTransferService {

	@Autowired
	CurrentAccountRepository cRepository;

	@Autowired
	SavingsAccountRepository sRepository;

	@Autowired
	TransferRepository transferRepository;

	public int transferToOthers(TransferOthersAccount transferOthers, String userName) {
		transferOthers.setUserName(userName);

		if (transferOthers.getAcc1().equals("Savings Account")) {
			SavingsAccount sAccount = sRepository.findByUserName(userName);
			float balance = sAccount.getBalance() - transferOthers.getAmount();
			if (balance >= 0) {
				sAccount.setBalance(balance);
				transferOthers.setBalance(sAccount.getBalance());
				if (transferOthers.getAcc2().equals("Savings Account")) {
					SavingsAccount sAccount2 = sRepository.findByAccountNumber(transferOthers.getAccNumber());
					sAccount2.setBalance(sAccount2.getBalance() + transferOthers.getAmount());
					sRepository.save(sAccount2);

				} else {
					CurrentAccount cAccount2 = cRepository.findByAccountNumber(transferOthers.getAccNumber());
					cAccount2.setBalance(cAccount2.getBalance() + transferOthers.getAmount());
					cRepository.save(cAccount2);

				}

				transferRepository.save(transferOthers);
				sRepository.save(sAccount);
				return 1;
			} else {
				return 0;
			}
		} else {
			CurrentAccount cAccount = cRepository.findByUserName(userName);
			float balance = cAccount.getBalance() - transferOthers.getAmount();
			if (balance >= 0) {
				cAccount.setBalance(balance);
				transferOthers.setBalance(cAccount.getBalance());
				if (transferOthers.getAcc2().equals("Savings Account")) {
					SavingsAccount sAccount2 = sRepository.findByAccountNumber(transferOthers.getAccNumber());
					sAccount2.setBalance(sAccount2.getBalance() + transferOthers.getAmount());
					sRepository.save(sAccount2);

				} else {
					CurrentAccount cAccount2 = cRepository.findByAccountNumber(transferOthers.getAccNumber());
					cAccount2.setBalance(cAccount2.getBalance() + transferOthers.getAmount());
					cRepository.save(cAccount2);

				}
				cRepository.save(cAccount);
				transferRepository.save(transferOthers);
				return 1;
			} else {
				return 0;
			}
		}
	}

}
