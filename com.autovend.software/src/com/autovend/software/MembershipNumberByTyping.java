package com.autovend.software;

import java.util.Scanner;
import com.autovend.MembershipCard;
import com.autovend.IllegalDigitException;

public class MembershipNumberByTyping {
    private boolean MembershipIsOperational = false;
    private static final int attemptLimit = 3;

    /**
     * Checks whether the given character is a digit or not.
     * @param c the character to be checked
     * @return true if the character is a digit, false otherwise
     */
    public boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    /**
     * Validates the given membership number by checking if it contains only digits and is 12 characters long.
     * Throws an IllegalDigitException if the membership number is invalid.
     * @param membershipNumber the membership number to be validated
     * @return true if the membership number is valid, false otherwise
     * @throws IllegalDigitException if the membership number is invalid
     */
    public boolean validateMembershipNumber(String membershipNumber) throws IllegalDigitException {
        if (membershipNumber == null || membershipNumber.length() != 12) {
            throw new IllegalDigitException("The membership number must be 12 digits.");
        }

        for (int i = 0; i < membershipNumber.length(); i++) {
            if (!isDigit(membershipNumber.charAt(i))) {
                throw new IllegalDigitException("The membership number can only have digits from 0 to 9.");
            }
        }

        return true;
    }

    /**
     * Returns the current status of the MembershipIsOperational field.
     * @return true if the membership is operational, false otherwise
     */
    public boolean getMembershipIsOperational() {
        return this.MembershipIsOperational;
    }

    /**
     * Prints a message indicating that the entered membership number is invalid.
     * Only prints the message if the number of attempts is less than the attempt limit.
     * @param numberOfAttempts the current number of attempts
     */
    private void printInvalidMembershipNumberMessage(int numberOfAttempts) {
        if (numberOfAttempts < attemptLimit) {
            System.out.println("The membership number you have entered is invalid, please enter a valid membership number or enter 'yes' if you would like to continue without one.");
        }
    }

    /**
     * Prompts the user to enter a membership number and validates it.
     * If the membership number is valid, it returns it. If not, it prompts the user to enter it again.
     * Throws an IllegalDigitException if the membership number is invalid.
     * @param scanner the Scanner object used for input
     * @return the validated membership number
     * @throws IllegalDigitException if the membership number is invalid
     */
    private String getMembershipNum(Scanner scanner) throws IllegalDigitException {
        int numberOfAttempts = 0;
        String membershipNumber = null;

        while (numberOfAttempts < attemptLimit) {
            System.out.println("Please enter your membership number: ");
            membershipNumber = scanner.nextLine();

            if (validateMembershipNumber(membershipNumber)) {
                return membershipNumber;
            }

            numberOfAttempts++;
            printInvalidMembershipNumberMessage(numberOfAttempts);
        }

        return null;
    }

    /**
     * Checks if the given membership number is valid.
     * @param membershipNumber the membership number to be checked
     * @return true if the membership number is valid, false otherwise
     */
    public boolean isValidMembershipNumber(String membershipNumber) {
        try {
            return validateMembershipNumber(membershipNumber);
        } catch (IllegalDigitException e) {
            return false;
        }
    }

    /**
     * Creates a new MembershipCard object with the given membership number and type of card.
     * @param membershipNumber the membership number for the card
     * @param typeOfCard the type of the card
     * @return the newly created MembershipCard object
     */
    public MembershipCard createMembershipCard(String membershipNumber, String typeOfCard) {
        return new MembershipCard("membership card", membershipNumber, typeOfCard, false);
    }

    /**
     * Updates the membership status by prompting the user to enter their membership information.
     * Prints the membership status and the information of the created MembershipCard object.
     * Throws an IllegalDigitException if the membership number entered by the user is invalid.
     */
    public void updateMembershipStatus() {
        try (Scanner scanner = new Scanner(System.in)) {
            MembershipCard membershipcard = new MembershipCard("membership card", "##########", "first_name last_name", false);
            System.out.println("Do you have a membership card? Please enter Yes/No/Cancel for which ever option applies to you.");
            String first_input = scanner.nextLine();

            if (first_input.equalsIgnoreCase("Yes")) {
                processYesResponse(scanner, membershipcard);
            } else if (first_input.equalsIgnoreCase("No")) {
                processNoResponse(scanner, membershipcard);
            } else if (first_input.equalsIgnoreCase("Cancel")) {
                System.out.println("Your membership registration has been cancelled successfully.");
                return;
            }

            System.out.println("membership status: " + MembershipIsOperational);
            System.out.println("membership card information: " + membershipcard.toString());
        } catch (IllegalDigitException e) {
            System.out.println(e.getMessage());
            System.out.println("Error, please try again.");
            updateMembershipStatus();
        }


    }

    /**
     * Processes the user's response when they indicate that they have a membership card.
     * Prompts the user to enter their membership number and creates a MembershipCard object with it.
     * Sets the MembershipIsOperational field to true if the membership number is valid.
     * Throws an IllegalDigitException if the membership number entered by the user is invalid.
     * @param scanner the Scanner object used for input
     * @param membershipcard the MembershipCard object to be created
     * @throws IllegalDigitException if the membership number is invalid
     */

    private void processYesResponse(Scanner scanner, MembershipCard membershipcard) throws IllegalDigitException {
        String membershipNumberByTyping = scanner.nextLine();
        boolean isValid = validateMembershipNumber(membershipNumberByTyping);
        if (isValid) {
            membershipcard = new MembershipCard("membership card", membershipNumberByTyping, "rewards member", false);
            MembershipIsOperational = true;
        } else {
            System.out.println("Registration will continue without a membership number.");
        }
    }

    /**
     * Processes the user's response when they indicate that they do not have a membership card.
     * Prompts the user to enter Yes or No to indicate whether they want to proceed without a membership number.
     * Calls the processYesResponse method if the user enters Yes.
     * Throws an IllegalDigitException if the membership number entered by the user is invalid.
     * @param scanner the Scanner object used for input
     * @param membershipcard the MembershipCard object to be created
     * @throws IllegalDigitException if the membership number is invalid
     */
    private void processNoResponse(Scanner scanner, MembershipCard membershipcard) throws IllegalDigitException {
        System.out.println("Would you like to proceed without a membership number? Please enter either Yes or No.");
        String second_input = scanner.nextLine();
        if (!second_input.equalsIgnoreCase("No") && !second_input.equalsIgnoreCase("Yes")) {
            System.out.println("The input you have provided is invalid. Please enter either Yes or No.");
            return;
        } else if (second_input.equalsIgnoreCase("No")) {
            processYesResponse(scanner, membershipcard);
        }
    }

}

