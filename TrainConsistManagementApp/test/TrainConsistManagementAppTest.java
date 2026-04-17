class TrainConsistManagementAppTest {
    private String type;

    public TrainConsistManagementAppTest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Bogie{" + "type='" + type + '\'' + '}';
    }
}