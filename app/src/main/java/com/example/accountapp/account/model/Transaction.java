@Entity
public class Transaction {
    @Id @GeneratedValue
    private Long id;

    private Long fromAccountId;
    private Long toAccountId;
    private double amount;
    private String currency;
    private String type; // "DEPOSIT", "WITHDRAWAL", "TRANSFER"

    private LocalDateTime date;

    // getters/setters
}
