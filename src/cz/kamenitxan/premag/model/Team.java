package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 08.09.15.
 */
@DatabaseTable
public class Team {
	@DatabaseField(generatedId = true)
	private int id = 0;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private School school;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Participant participant1;
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private Participant participant2;
	@DatabaseField
	private String experiment;
	@DatabaseField
	private String requirments;
	@DatabaseField
	private boolean lunch;
	@DatabaseField
	private int year;
	@DatabaseField
	private int correctnessScore;
	@DatabaseField
	private int skillScore;
	@DatabaseField
	private int catchinessScore;
	@DatabaseField
	private int entryOrder;

	public Team() {
	}

	public int getId() {
		return id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Participant getParticipant1() {
		return participant1;
	}

	public void setParticipant1(Participant participant1) {
		this.participant1 = participant1;
	}

	public Participant getParticipant2() {
		return participant2;
	}

	public void setParticipant2(Participant participant2) {
		this.participant2 = participant2;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	public String getRequirments() {
		return requirments;
	}

	public void setRequirments(String requirments) {
		this.requirments = requirments;
	}

	public boolean isLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTotalScore() {
		return correctnessScore + skillScore + catchinessScore;
	}


	public int getCorrectnessScore() {
		return correctnessScore;
	}

	public void setCorrectnessScore(int correctnessScore) {
		this.correctnessScore = correctnessScore;
	}

	public int getSkillScore() {
		return skillScore;
	}

	public void setSkillScore(int skillScore) {
		this.skillScore = skillScore;
	}

	public int getCatchinessScore() {
		return catchinessScore;
	}

	public void setCatchinessScore(int catchinessScore) {
		this.catchinessScore = catchinessScore;
	}

	public int getEntryOrder() {
		return entryOrder;
	}

	public void setEntryOrder(int entryOrder) {
		this.entryOrder = entryOrder;
	}
}
