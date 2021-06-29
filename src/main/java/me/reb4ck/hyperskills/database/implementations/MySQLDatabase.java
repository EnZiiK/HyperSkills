package me.reb4ck.hyperskills.database.implementations;

import me.reb4ck.helper.database.Credentials;
import me.reb4ck.hyperskills.HyperSkills;
import me.reb4ck.hyperskills.database.SQLDatabase;

public class MySQLDatabase extends SQLDatabase {

	public MySQLDatabase(HyperSkills parent, Credentials credentials) {
		super(parent);
		this.connect(credentials);
	}
}