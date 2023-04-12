# SENG-300-Iteration-3

## Group Managers: Marianna Ferreira and Gabriel Rodriguez

### Objectives: To maintain/update a list of tasks, including various meeting agendas, set/meet due dates, and to organize the group.

## SENG 300 Project, Iteration 3 and Project Demonstration

# Meeting 1:
🎯 DUE DATE: 2023 APR 12 23:59

</aside>

- Group Members and UCIDs
    - this is what to be pasted on top of every source code we create!
    
    ```python
    // P3-4 Group Members
    //
    // Abdelrhafour, Achraf (30022366)
    // Campos, Oscar (30057153)
    // Cavilla, Caleb (30145972)
    // Crowell, Madeline (30069333)
    // Debebe, Abigia (30134608)
    // Dhuka, Sara Hazrat (30124117)
    // Drissi, Khalen (30133707)
    // Ferreira, Marianna (30147733)
    // Frey, Ben (30088566)
    // Himel, Tanvir (30148868)
    // Huayhualla Arce, Fabricio (30091238)
    // Kacmar, Michael (30113919)
    // Lee, Jeongah (30137463)
    // Li, Ran (10120152)
    // Lokanc, Sam (30114370)
    // Lozano Cetina, Jose Camilo (30144736)
    // Maahdie, Monmoy (30149094)
    // Malik, Akansha (30056048)
    // Mehedi, Abdullah (30154770)
    // Polton, Scott (30138102)
    // Rahman, Saadman (30153482)
    // Rodriguez, Gabriel (30162544)
    // Samin Rashid, Khondaker (30143490)
    // Sloan, Jaxon (30123845)
    // Tran, Kevin (30146900)
    //
    ```
    

## Agenda for Monday Meeting (Apr 3 17:00 thru Discord)

---

[](https://d2l.ucalgary.ca/d2l/le/content/497088/viewContent/5589946/View)

[](https://d2l.ucalgary.ca/d2l/le/content/497088/viewContent/5589956/View)

- [x]  check names and ucids for any corrections
- [ ]  read through project iteration and demonstration descriptions
    - Project Iteration 3 DESCRIPTION
        - [ ]  which project iteration 2 code do we build on?
            - Ach and Marianna’s ?
            - Oscar’s?
        - OVERVIEW: The third and final iteration is the most complex yet. Your team is now big and simply organizing and managing it will become a task in itself.
        - download **com.autovend.hardware_0.3.** Do not alter.
        - tasks
            - [ ]  complete all remaining use cases in use case model (v3.0)
            
            [](https://d2l.ucalgary.ca/d2l/common/dialogs/quickLink/quickLink.d2l?ou=497088&type=coursefile&fileId=Autovend+UC+3.pdf)
            
            - [ ]  update and complete existing use cases from previous iterations
            - [ ]  develop a GUI for self-checkout station and for attendant station using Java Swing
                - You will need GUIs for each of a set of DoItYourselfStations and an AttendantStation. I would suggest that you consider providing separate GUIs to simulate customer input, attendant input, and provide a means of showing what change is returned and what is printed on the receipt.
            - [ ]  decide which repo to build on
            - [ ]  automated test suite for testing. junit. gui tested by interation
            - [ ]  begin source file with comment with members’ names above
            - [ ]  optional supplementary one page explanation about how application works
            - [ ]  provide a git log
            - [ ]  detailed structure diagram to explain how application works
                - [ ]  in this situation, what is important is to be complete in terms of the types, packages, and relationships between all of these. You should only show dependencies, containment, and generalization. Do not show association, aggregation, or composition: each of these implies dependency as well, so just show us the dependencies.
            - [ ]  prepare and record a demonstration of your final product
            - ASPECTS OF WORK (to assign later)
                
                There are multiple aspects of the work to be considered here:
                
                - design;
                - implementation;
                - automated testing, bug reports, bug repairs;
                - documenting;
                - managing and tracking;
                - demonstrating.
            - ask for knowledges and skill strengths
                - for team management (see below)
- [ ]  organisation decisions
    - [ ]  team structure
        - subgroups system?
        - groupings (4 groups of 6? + 1 project manager)
            - 💡 CURRENT IDEA: DIVIDE AND CONQUER APPROACH, HIERARCHY
                - good # of groups? 6 groups of 4 people. (2 for soft dev, 2 for testing?):
                    - subgroup can have their own meetings
                        - then at least one representative per each subgroup with main group
                    - how do tasks get assigned in one group?
        - teams:
            - design/implementation team [FULL; DONE]
                
                > 12 people
                > 
                > - ASSIGNED:
                >     1. Achraf Abdelrhafour
                >     2. Ran Li
                >     3. Ben F
                >     4. Sara Dhuka
                >     5. Jeongah (Anna) Lee
                >     6. Jose Lozano
                >     7. Jaxon Sloan
                >     8. Saadman
                >     9. Abigia Debebe
                >     10. Mehedi
                >     11. Caleb Cavilla
                >     12. Oscar Campos
                - traditional coding tasks
                - software development
                - GUI tasks
                - [ ]  Elect managers:
                    - [ ]  1 Product Manager (making sure customer requirements are met),
                    - [ ]  1 Software/Design Manager (making sure the code-base is setup and all use cases are implemented correctly),
                    - [ ]  1 UI Manager (divides up the front-end work),
            - management team [FULL; DONE]
                
                > 2 people:
                > 
                > - ASSIGNED:
                >     1. Ferreira, Marianna
                >     2. Rodriguez, Gabriel  
                - coordinate meetings
                - communicate with all group members
                - manage tasks
                - assign tasks
                - track completion status
                - scheduling and time management
                - make the readme
            - quality assurance team [FULL; DONE]
                
                > 6 people
                > 
                > - ASSIGNED:
                >     1. Kevin Tran
                >     2. Akansha Malik
                >     3. Monmoy Maahdie
                >     4. Scott Polton
                >     5. Sam Lokanc
                >     6. Khondaker Samin Rashid
                - create automated test cases,
                - run them,
                - write bug reports
                - repair
                - [ ]  Elect a Manager:
                    - [ ]  1 Quality Assurance Manager (divides up testing work).
                    - Quality Assurance Team Meeting Agenda
                        - ASSIGNED PEOPLE:
                            1. Tran, Kevin
                            2. Akansha Malik
                            3. Monmoy Maahdie
                            4. Scott Polton
                            5. Sam Lokanc
                            6. Samin Rashid, Khondaker
                        - [ ]  Attendance
                        - [ ]  Effects of Design/Implementations Due Date Extension
                        - [ ]  Code Explanation with Jose
                        - [ ]  Task Allocation
                        - [ ]  Manager Election
                        - [ ]  Scheduling and Due Dates
                        - [ ]  Team Timeline
            - documentation team [FULL; DONE]
                
                > 3 people
                > 
                > - ASSIGNED:
                >     1. Drissi, Khalen
                >     2. Tanvir Ahamed Himel
                >     3. Huayhualla, Francisco
                - create required models
            - demonstration team [FULL; DONE]
                
                > 2 people
                > 
                > - ASSIGNED:
                >     1. Michael Kacmar
                >     2. Madeline
                - make demo video no longer than 10 minutes
                - aim for how customer can interact w ur product to conduct various use cases.
    - [ ]  task allocation
    - [ ]  scheduling
        - [ ]  add a buffer between actual due date and group due date.
            
            > actual project due date: WEDNESDAY, APR 12 23:59
            > 
            
            > group overall due date: THURSDAY, APR 11 23:59
            > 
            > 
            > > software dev due dates:
            > > 
            > > - starts: ********************************MONDAY, APR 03 00:01********************************
            > > - ends: **~~FRIDAY, APR 07 23:59~~ SATURDAY, APR 08 23:59***
            > 
            > > testing due dates:
            > > 
            > > - starts: ********************************THURSDAY, APR 06 00:01********************************
            > > - ends: **TUESDAY, APR 11 23:59**
            > 
            > > documentation due date:
            > > 
            > > - starts: **SUNDAY, APR 09 23:59**
            > > - ends: **TUESDAY, APR 11 23:59**
            > 
            > > demonstration due date:
            > > 
            > > - starts: **SUNDAY, APR 09 23:59**
            > > - ends: **TUESDAY, APR 11 23:59,**
            > > - ~~emergency due date: **WEDNESDAY, APR 12**~~
            > >     - **~~if structure diagram is lacking.~~**
            > 
            > > potential cram day: **TUESDAY-WEDNESDAY, APR 11-12**
            > > 
            > 
            > > submit day: **WEDNESDAY, APR 12 20:00**
            > > 
            > 
    - [ ]  project timeline
        - subgroup meetings
        - 📅 official project timeline
            
            
            | Date | Tasks |
            | --- | --- |
            | APR 3 | First Meeting, Git Repo Setup |
            | APR 4 | Subgroup Meeting for Dev or Testing Team, could start work from then on.  |
            | APR 5 | Subgroup Meeting for Testing Team |
            | APR 6 | Testing could begin; |
            | APR 7 (Holiday) | Expectation: Software dev should be finished. |
            | APR 8 | Testing continues, Docu and Demo could start early. |
            | APR 9 | Docu and Demo starts |
            | APR 10 (Holiday) | Work Period |
            | APR 11 | Expectation Testing over. Demo over, Docu over. FINAL MEETING (see when2meet) |
            | APR 12 | PROJECT DUE + DEMONSTRATION. Submit + Cramming |
            
            > THESE DATES ARE NOT FIXED, YOU GUYS COULD WORK AHEAD IF Y’ALL WANT.
            > 
    - [ ]  discord management (dicsord roles)
- [ ]  getting started w the project
    - [ ]  who should own our group’s git repo?
        - git management
        - git branching rules
        - share git repo link to links channel
    - [ ]  raise potential issues experienced from previous iterations
        - git merging issues?
        - coworker issues?
- [ ]  who wants to submit: **Jose Lozano.**
    - [ ]  WHAT TO SUBMIT:
        - [ ]  your diagrams (plus a cover page with the names and student numbers of 
        your teammates, plus an optional explanation page) as a single **PDF document**
        - [ ]  com.autovend.software
        - [ ]  com.autovend.software.test
        - [ ]  git log for team’s git repo
        - include in comments. names+UCID of members, external sources of info
- [ ]  reminders
    - don’t forget to do peer evaluations!!!
    - next meeting: ???

### *Factors affecting your grade*

- Completeness of the functionality implemented
- Quality of the automated test suite implemented
- Coverage (instruction and branch) of your functionality by your test suite
- Adherence to properties related to "clean code"
- **Adherence to the six design properties discussed in lecture**
- Conformance to the requirements specified in these instructions
- Adherence to general properties of good engineering practice (i.e.,
it will not be acceptable for you to come up with some "clever scheme"
to "get around" other requirements: don't be a lawyer)
- Lateness
- Individual contribution
- Submission of your peer/self evaluation
- Adherence to general properties of academic integrity and the Collaboration and Plagiarism Policy of this course

---

## Questions?

---

- Trello is optional.
- Java 18?
- **Eclipse!** IntelliJ?

- Check names and UCIDs for any corrections
- Read through project iteration and demonstration descriptions
- Decide which repo to build on
- Create GUIs for self-checkout station and attendant station using Java Swing
- Complete all remaining use cases in use case model (v3.0)
- Update and complete existing use cases from previous iterations
- Automated test suite for testing, JUnit, GUI tested by iteration
- Provide a git log
- Prepare and record a demonstration of the final product
- Organizational decisions:
    - Team structure
    - Task allocation
    - Scheduling
    - Discord management (roles)
- Getting started with the project:
    - Git management
    - Raise potential issues experienced from previous iterations
- Submission details
- Don't forget to do peer evaluations

## Agendas for Meetings!

---

__APRIL 04 MEETING PLAN - DESIGN AND IMPLEMENTATION TEAM__
**Things to discuss:**

  - Git Repo setup, check everyone's access
      -Everyone on eclipse?
      -Discuss git branching rules

  - Divide up the work (agree on who does what!)
      -By use case? by similar function??

  - Elect managers:
    - [ ]  1 Product Manager (making sure customer requirements are met),
    - [ ]  1 Software/Design Manager (making sure the code-base is setup and all use cases are implemented correctly)
    - [ ]  1 UI Manager (divides up the front-end work)

__APRIL 04 MEETING MINUTES__ 

  - Managers Elected:
    - Product Manager: Ben
    - Software/Design Manager: Jose
    - UI Manager: Jaxon

  - Dividing the work
    **-Front End (5) vs Back end (7) team**
    - Front End: Jaxon, Ben, Sara, Mehedi, Oscar 
          - Java SWING: get familiar with tutorials (By tomorrow!!)
    - Back end: Jose, Ach, Saadman, Anna, Abi, Caleb, Ran
       - Design pattern (Model-View-Controller): view, controller (logic) (https://www.youtube.com/watch?v=dTVVa2gfht8)
          - Pay: Abi, Caleb, Saadman
          - Add Item: Ran, Anna
          - The rest: floaters

GUI (1 DoItYourselfStation, 1 AttendantStation):
    **DoItYourselfStation:**
    -> start screen
     -> view for adding items
      -> Separate window for payment
      -> Show print receipt
    **AttendantStation:**
    -> exception handling (weight discrepancy, etc)

**Software Due Dates:**
Approx: FRIDAY, APR 07 23:59

**Manager Meeting:**
- FRIDAY, APR 07 xx:xx


__Quality Assurance Team Meeting Agenda__  
FRIDAY, APR 07 19:30-20:30

   -> ASSIGNED PEOPLE:
        1. Kevin Tran  
        2. Akansha Malik 
        3. Monmoy Maahdie 
        4. Scott Polton 
        5. Sam Lokanc 
        6. Samin Rashid, Khondaker  

    -> []  Attendance  (5/6 people attended)
    -> []  Effects of Design/Implementation Due Date Extension
    -> []  Code Explanation with someone from Design/Implementation Team
    -> []  Task Allocation, 
                  >> refer to Kevin's split of categories: 
                             split by use case category?
                                     Akansha. - ITEMS.
                                     Kevin. - PAYMENT.
                                     Monmoy. - ATTENDANT-SPECIFIC EVENTS/ACTIONS.
                                     Scott. - MEMBERSHIP AND MISCELLANEOUS.
                                     Sam. - STATION MAINTENANCE.
                                     <VACANT> - ANY SUBJECT TO CHANGE (WILD CARD).
                                     These are not hard lines, you don't have to stick with this. This is the bare minimum of task y'all can do.
                  >> see Jose's notion's breakdown:
    -> []  Manager Election: Kevin (cooperation, checking-in) and Scott (technical, inter team relations)
    -> []  Scheduling and Due Dates: TEAM DUE DATE: TUESDAY, APR 11 23:59
    -> []  Team Timeline
                      >> Start working by Tonight/Tomorrow. Expect finish by Due Date.
                      >> Work Days: FRIDAY, SATURDAY, SUNDAY, MONDAY, TUESDAY
                      >> Check-In Meetings: SATURDAY, MONDAY or SUNDAY, TUESDAY
   -> [ ] Contact and Catch Tusher Up.
   
   
__Demonstration Team Meeting Agenda__  
SUNDAY, APR 08 16:00-16:30

-> ASSIGNED PEOPLE:
     1. Crowell, Madeline  
     2. Kacmar, Michael 
 
-> [] Attendance (2/2 people attended)
-> [] Go through task description at SENG 300 D2L
-> [] Code Explanation and GUI Walkthrough (with current progress) from someone in Des/Imp Team, Front-End Band
-> [] Task Allocation
               Use Case Go Through <- Madeline makes a chart for runthroughs
               Testing UI  <- Jaxon could make it by further notice.
               Recording <- Split between both members
               Editing       <- Split between both members

-> [] Scheduling and Due Dates: should be finished and ready to submit by Wednesday.
-> [] Team Timeline
                   SUNDAY: Customer UI finished? Attendant UI probably tomorrow? Pick runthroughs per person <- wait for Madeline making a chart for Runthroughs
                   MONDAY: Have someone from Des/Imp team, check if the implementation matches y'all's runthrough chart. Go Through the Customer UI and Assess which parts of the workload can be preponed. Also possibly record.
                   TUESDAY: Record the video? Edit the video? 
                   WEDNESDAY: Edit the video? (reach out to management in case help needed). Hopefully, submission with the project code at the same time.
 
-> [] Breakdown of Runthroughs <- Madeline, once finished then decide from there.
                    Multiple Use Cases: different payment methods, issues, try to incorporate multiple use cases in multiple walkthroughs
                     3 major runthroughs, 1 start up/shut down, prevent station use., 
                     
                     
__Quality Assurance Meeting Agenda__  
SUNDAY, APR 08 18:30-19:00

OBJECTIVES:
[ ] Attendance (QA team members mainly, optionally someone from dev or management teams maybe?)
[ ] Progress Check-in 
[ ] Any conficts??
[ ] Fixing Git testing branch, merge, etc. if needed
[ ] Allocate who will test customer classes, other classes, and redistribute tasks if needed


__FINAL GROUP MEETING__  
TUESDAY, APR 11 19:00 - 20:00

WHO: 
Managers in @design/implementation team  and @quality assurance team , AND ALL Members of the @documentation team , @demonstration team , and @management team   are required to attend it! For the rest, it's not required unless you want to raise an issue or report an HR conflict such as an uncooperative groupmate (which u can do anyways via DMing Marianna and/or I!)

AGENDA:
[X] Attendance (see people above)
[X] Hardware Adjustment Status Check (how it affects everyone)
[X] Team Progress Reports (we're gonna ask you what the state of your task completion so far).
- go over demo pdf
[X] Submission Plans - Project: still Jose, Demo: Madeline.,  meeting before submission?
[X] Peer Evaluation & Issue Reports
[X] Contingency Planning - New Action Plan (in the case, where we are lagging in terms of progress and we need to use our buffer cram time) <- also talk about partial completion for hardware, and check if it allows other teams to work simultaneously alongside with des/imp team dealing with the recent adjustments
[] Other Housekeeping Stuff

---
