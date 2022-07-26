package com.android.riccardo.myapplication.misc;

public class DBArtist {
	private int id;
	private String name = null;
    private Integer listenCount = null;

    public DBArtist(int id, String name, Integer listenCount) {
        this.id = id;
        this.name = name;
        this.listenCount = listenCount;
    }

    public DBArtist(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DBArtist(int id) {
		super();
		this.id = id;
	}

	public DBArtist() { super(); }

	public int getId() {
		return id;
	}

	public String getName() {return name;}
    public int getListenCount() {
        return listenCount;
    }

    public String toString() {
        String str = "Artist{ id='" + id + '\'';
        if (name != null)
            str += " name='" + name + '\'';
        if (listenCount != null)
            str += " listenCount='" + listenCount + '\'';
        str += '}';
        return str;
    }
}
