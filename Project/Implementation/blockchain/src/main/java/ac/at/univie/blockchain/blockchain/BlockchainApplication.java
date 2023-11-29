package ac.at.univie.blockchain.blockchain;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlockchainApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(BlockchainApplication.class, args);

		CLI cli = new CLI();

		cli.startCLI();

	}

}
