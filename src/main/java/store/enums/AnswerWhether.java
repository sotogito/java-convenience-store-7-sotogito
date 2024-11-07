package store.enums;

public enum AnswerWhether {
    YES("Y", true),
    NO("N", false);

    private final String answer;
    private final boolean meaning;

    AnswerWhether(String answer, boolean meaning1) {
        this.answer = answer;
        this.meaning = meaning1;
    }

    public static boolean findMeaningByAnswer(String answer) {
        return answer.equals(answer);
    }


}
