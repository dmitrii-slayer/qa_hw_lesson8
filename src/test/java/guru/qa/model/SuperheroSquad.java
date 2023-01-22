package guru.qa.model;

import java.util.ArrayList;

public class SuperheroSquad {
    public String squadName;
    public String homeTown;
    public int formed;
    public String secretBase;
    public boolean active;
    public ArrayList<Member> members;

    public static class Member {
        public String name;
        public int age;
        public String secretIdentity;
        public ArrayList<String> powers;
    }
}
