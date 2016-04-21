package com.facecheck;

public class StuInfo  {

	private String idNumber;
	private String name;
	private String stuClass;
	private String field;
	private String gender;

	public StuInfo() {
		super();
	}

	public StuInfo(String idNumber, String name,
			String stuClass, String field,String gender) {
		super();
		this.idNumber = idNumber;
		this.name = name;
		this.stuClass = stuClass;
		this.field = field;
		this.gender =gender;
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public String getName() {
		return this.name;
	}

	public String getStuClass() {
		return this.stuClass;
	}

	public String getField() {
		return this.field;
	}
	public String getGender(){
		return this.gender;
	}
}
