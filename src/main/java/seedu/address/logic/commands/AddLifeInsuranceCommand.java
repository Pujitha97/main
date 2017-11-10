package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BENEFICIARY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTRACT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PREMIUM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIGNING_DATE;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.insurance.LifeInsurance;
import seedu.address.model.insurance.ReadOnlyInsurance;
import seedu.address.model.insurance.exceptions.DuplicateInsuranceContractNameException;
import seedu.address.model.insurance.exceptions.DuplicateInsuranceException;
import seedu.address.model.person.ReadOnlyPerson;

//@@author OscarWang114
/**
 * Creates a life insurance in Lisa
 */
public class AddLifeInsuranceCommand extends UndoableCommand {
    public static final String[] COMMAND_WORDS = {"addli", "ali", "+li"};
    public static final String COMMAND_WORD = "addli";

    public static final String MESSAGE_USAGE = concatenateCommandWords(COMMAND_WORDS)
            + ": Adds a life insurance to Lisa. "
            + "Parameters: "
            + PREFIX_NAME + "INSURANCE_NAME "
            + PREFIX_OWNER + "OWNER "
            + PREFIX_INSURED + "INSURED "
            + PREFIX_BENEFICIARY + "BENEFICIARY "
            + PREFIX_PREMIUM + "PREMIUM "
            + PREFIX_SIGNING_DATE + "SIGNING_DATE "
            + PREFIX_EXPIRY_DATE + "EXPIRY_DATE"
            + PREFIX_CONTRACT_NAME + " CONTRACT_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Term Life "
            + PREFIX_OWNER + "Alex Yeoh "
            + PREFIX_INSURED + "John Doe "
            + PREFIX_BENEFICIARY + "Mary Jane "
            + PREFIX_PREMIUM + "600 "
            + PREFIX_SIGNING_DATE + "17 11 2017 "
            + PREFIX_EXPIRY_DATE + "17 11 2037 "
            + PREFIX_CONTRACT_NAME + "AlexYeoh-TermLife";

    public static final String MESSAGE_SUCCESS = "New insurance added: %1$s";
    public static final String MESSAGE_DUPLICATE_INSURANCE_CONTRACT_FILE =
            "This insurance contract file already exists in LISA";

    private final LifeInsurance lifeInsurance;

    /**
     * Creates an {@code AddLifeInsuranceCommand} to add the specified {@code LifeInsurance}
     */
    public AddLifeInsuranceCommand(ReadOnlyInsurance toAdd) {
        lifeInsurance = new LifeInsurance(toAdd);
    }

    /**
     * Check if any {@code ReadOnlyPerson} arguments (owner, insured, and beneficiary) required to create a
     * life insurance are inside the person list by matching their names case-insensitively. If matches,
     * set the person as the owner, insured, or beneficiary accordingly. Note that a person can
     */
    private void isAnyPersonInList(List<ReadOnlyPerson> list, LifeInsurance lifeInsurance) {
        String ownerName = lifeInsurance.getOwner().getName();
        String insuredName = lifeInsurance.getInsured().getName();
        String beneficiaryName = lifeInsurance.getBeneficiary().getName();
        for (ReadOnlyPerson person: list) {
            String lowerCaseName = person.getName().toString().toLowerCase();
            if (lowerCaseName.equals(ownerName)) {
                lifeInsurance.setOwner(person);
            }
            if (lowerCaseName.equals(insuredName)) {
                lifeInsurance.setInsured(person);
            }
            if (lowerCaseName.equals(beneficiaryName)) {
                lifeInsurance.setBeneficiary(person);
            }
        }
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        List<ReadOnlyPerson> personList = model.getFilteredPersonList();
        isAnyPersonInList(personList, lifeInsurance);
        try {
            model.addLifeInsurance(lifeInsurance);
            return new CommandResult(String.format(MESSAGE_SUCCESS, lifeInsurance));
        } catch (DuplicateInsuranceException die) {
            throw new AssertionError("AddressBooks should not have duplicate insurances");
        } catch (DuplicateInsuranceContractNameException dicne) {
            throw new CommandException(MESSAGE_DUPLICATE_INSURANCE_CONTRACT_FILE);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddLifeInsuranceCommand); // instanceof handles nulls
                //&& toAdd.equals(((AddLifeInsuranceCommand) other).toAdd));
        //TODO: need to compare every nonstatic class member.
    }

    //@@author arnollim
    @Override
    public String toString() {
        return COMMAND_WORD;
    }
    //@@author
}
