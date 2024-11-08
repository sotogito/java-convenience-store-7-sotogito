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

    public static boolean findMeaningByAnswer(AnswerWhether userAnswer) {
        for (AnswerWhether answerWhether : AnswerWhether.values()) {
            if (answerWhether.equals(userAnswer)) {
                return answerWhether.meaning;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
    }


    public static AnswerWhether findByInputAnswer(String inputAnswer) {
        for (AnswerWhether answerWhether : AnswerWhether.values()) {
            if (answerWhether.answer.equals(inputAnswer)) {
                return answerWhether;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다. 다시 입력해 주세요.");
    }

}
