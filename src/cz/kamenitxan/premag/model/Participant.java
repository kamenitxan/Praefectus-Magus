package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 13.09.15.
 */
@DatabaseTable
public class Participant {
	@DatabaseField(generatedId = true)
	private int id = 0;
	@DatabaseField
	public String firstName;
	@DatabaseField
	public String lastName;
	@DatabaseField
	public String birthDate;
	@DatabaseField
	public String className;
}
