package com.facecheck;

public class MyClass  {

	private String id;
	private String name;
	private String teacherName;
	private String timeLocation;

	public MyClass() {
		super();
	}

	public MyClass(String id, String name,
			String teacherName, String timeLocation) {
		super();
		this.id = id;
		this.name = name;
		this.teacherName = teacherName;
		this.timeLocation = timeLocation;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getTeacherName() {
		return this.teacherName;
	}

	public String getTimeLocation() {
		return this.timeLocation;
	}
}
