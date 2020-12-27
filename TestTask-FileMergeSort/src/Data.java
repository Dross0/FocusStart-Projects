import java.util.Optional;

public class Data implements Comparable<Data> {
    private final InputDataType dataType;
    private final String string;
    private Integer integer;

    public Data(String string) throws StringToIntCastException {
        this(string, InputDataType.STRING);
    }

    public Data(String string, InputDataType type) throws StringToIntCastException {
        this.dataType = type;
        this.string = string;
        if (type == InputDataType.INTEGER){
            try{
                this.integer = Integer.parseInt(string);
            }
            catch (NumberFormatException ex){
                throw new StringToIntCastException(ex.getMessage());
            }
        }
    }

    public Optional<Integer> getInteger() {
        return Optional.ofNullable(integer);
    }

    public String getString() {
        return string;
    }

    @Override
    public int compareTo(Data data) {
        if (dataType == InputDataType.INTEGER && data.dataType == InputDataType.INTEGER){
            return this.integer - data.integer;
        }
        return this.string.compareTo(data.string);
    }
}
