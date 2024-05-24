# contributions

## Sprint 1

### Week 1 (24/04/10~24/04/17)

* Zizhou Luo

  progress on implementing User Management System(#4,#7,#9): Registration, Login, Logout

  * modify User Class in `User.java`, add several fields, corresponding methods and corresponding mapping in `DTOMapper.java`
  * implement help methods in `UserService.java` to manipulate database and provide corresponding endpoint in `UserController.java`

  create `contributions.md` to keep track of weekly contribution

  add comments to make the code more readable 
  
* Ziqi Yang

   1.implement register page in front-end(#2)

    * write register page ui and write suitable API to post back username, password and registration code(optinal).
    * implement router of register page

   2.implement lobby page in front-end(#7)

    * write lobby page ui and write suitable function of this page which is navigating to two main topics Mensa and Course.
    * implement router of lobby page
  
* Zheyuan Fu

   1.implement login page in front-end(#3)

    * write login page ui and write suitable API to put back username, password and let back-end to confirm them.
    * implement router of login page

   2.implement header in front-end(#7)

    * write header ui and write suitable function of this module which is navigating to lobby page, search page and profile page.
    * paint search icon and profile icon for header
  
   3.implement createitem page in front-end(#5)
  
    * write createitem page ui and write suitable API to post back topicId and let back-end to create new item under the topic.
    * implement router of createitem page



### week 2

#### Week 2 (24/04/17~24/04/24)

* Zizhou Luo

  progress on comment and scoring system (#15) and other user related methods

  progress on frontend and backend integration

* Yiming Xiao

    Finish implementing Topic System as per User stories.

    * Related files include `Topic.java`, `TopicController.java`, `TopicService.java`, `TopicRepository.java`, `TopicGetDTO.java`, `TopicPostDTO.java`in the project.

    Finish implementing Item System as per User stories.

    * Related files include `Item.java`, `ItemController.java`, `ItemService.java`, `ItemRepository.java` in the project.

    Modify the function 'Ranking' and 'Get the average score' as per discussion with Zizhou.

    * Delete the function related code in item system and use it from Comment system.
    
* Ziqi Yang

     1.implement createtopic page in front-end(#10)

      * write createtopic page ui and write suitable API to post back topicName and let back-end to create new topic by using the topicName
      * implement router of createtopic page
    
     2.implement and modify profile page in front-end(#19)
    
      * write profile page ui and write suitable API to get back username and creation date from back end
      * write suitable API to get back followitem List from backend and write a new ui to display it.
      * write suitable API to get back commentlist which includes user's own comment from backend and write a new ui to display it.
      * implement router of profile page
    
     3.modify comment page in front-end(#8)
    
      * modify API of comment page to get back item introduction from back-end.
    
* Zheyuan Fu

     1.implement comment page in front-end(#8)

      * write comment page ui and write suitable API to get back itemName, itemIntroduction.
      * write suitable API to post back commentContent. commentOwnerName, commentOwnerId to back-end.
      * write suitable API to get back commentContent. commentOwnerName, commentOwnerId from back-end and display it in this page.
      * implement router of comment page
    
     2.implement chatspace in front-end(#8)
    
      * write chatspace ui and write suitable function of this module which is a platform for online user to chat in this space.
      * modify chatspace ui and make it display in a suitable place in comment page.

## Sprint 2

#### Week 1 (24/05/01~24/05/08)

* Yiming Xiao

    Finish test all functions in Topic Controller.

    * Related files include `TopicController.java`, `TopicControllerTest.java`in the project.

    Finish test all functions in Item Controller.

    * Related files include  `ItemController.java`, `ItemControllerTest.java` in the project.

    Add Translate funtion and finish testing.

    * Related files include  `TranslateController.java`, `TranslateControllerTest.java` in the project.

    

### Week 2 (24/05/08~24/05/15)

* Zizhou Luo

  Change database from H2 to MySQL to persist the data

  * Create Google Cloud MySQL instance and test connection
  * modified configuration files `application.properties`&`build.gradle` and relative test `test\repository\*`

  Learning WebSocket 


* Yiming Xiao

    Finish test all functions in TopicService and TopicRepository.

    * Related files include `TopicService.java`, `TopicServiceTest.java`, `TopicRepository.java`, `TopicRepositoryTest.java`in the project.

    Finish test all functions in ItemService and ItemRepository.

    * Related files include `ItemService.java`, `ItemServiceTest.java`, `ItemRepository.java`, `ItemRepositoryTest.java`in the project.

    Finish test Topic & Item parts in DTOMapper.

    * Related file `DTOMapper.java` in the project.
    
* Ziqi Yang

     1.implement search page in front-end(#21)

      * write search page ui and write suitable API to get back itemName, TopicName from back-end by keyword.
      * write a select module to switch the search type from item to topic or from topic to item.
      * implement router of search page

     2.add thumbsup function in comment page in front-end(#11)

      * write thumbsup function which can count handle thumbsup request. send thumbup operation to back-end and get back total thumbsup number from back-end
      * write suitable Ui of it.
    
*  Zheyuan Fu

     1.modify commentList in comment page(#13)

      * write suitable ui to display all replies
      * write suitable ui to send reply to comment
      * write a suitable API to post back a repliy to this comment to back-end.


     2.add topicList page (#17)

      * write topiclist page ui and write suitable API to get back all topics from back-end.
      * add create topic function to let user can create new topic 
      * implement router of topiclist page

### Week 3 (24/05/15~24/05/22)

* Yiming Xiao

1. **Add function as per Frontend requirement**
    * Search items by keywords: Search for items by keywords so that I can efficiently find items of interest.
    * Get popular item:  Get which is most popular item
    * Get items sorted by Comment Count
    * Related Files:
        - `Item.java`,`ItemService.java`, `ItemController.java`,
        - `ItemTest.java`,`ItemServiceTest.java`,`ItemControllerTest.java`
2. **Refactor DTO Classes:**
    - ItemPostDTO:
        - Removed default value initialization from field declarations and moved them to the constructor.
        - Ensured type consistency and improved code readability.
    - TopicPostDTO:
        - Unified `Integer` and `Long` types for consistency.
        - Streamlined the code for better maintainability.
    - ItemGetDTO:
        - Standardized method names to match field names.
        - Removed unnecessary blank lines and optimized code structure.
    - TopicGetDTO:
        - Consistently used `Integer` and `Long` types.
        - Improved code readability and maintainability.
    - Related Files:
        - `ItemPostDTO.java`,`ItemGetDTO.java`, `TopicPostDTO.java`,`TopicGetDTO.java`,`ItemService.java`,`ItemServiceTest.java`
3. **Add Comprehensive Unit Tests:**
    - ItemPostDTOTest:
        - Added tests for `addLike` and `incrementPopularity` methods.
        - Included tests for all getters and setters to ensure proper functionality.
    - ItemGetDTOTest:
        - Added tests for `addLike` and `incrementPopularity` methods.
        - Included tests for all getters and setters to ensure proper functionality.
    - ItemServiceTest:
        - Added missing tests for `getItemsSortedByPopularity` and `searchItemsByKeyword` methods.
        - Added a test for the `getItemsSortedByCommentCountAndTopicId` method to handle cases with no comments.
4. **Update README and LICENSE Files:**
    - README.md:
        - Updated project description and usage instructions.
        - Added detailed setup and configuration steps.
        - Included contribution guidelines.
    - LICENSE:
        - Added the appropriate license for the project to ensure proper usage and distribution.



* Zizhou Luo

1. **Implement WebSocket**

   * WebSocket Endpoint, handling and saving chat message
   * settle WebSocket connection problem in production

2. **Reply functions**

   * add field `fatherCommentId` to indicate whether a `comment` is a reply
   * `getCommentByFatherCommentId` will return all reply to a specific comment
   * reply have no score and will not be counted into average score of a item, average score item can be update correctly 

3. **Modify CommentGetDTO/ReplyGetDTO**

   user `@Mappings` notation to wrap the username and avatar of the owner of comment/reply for display by the frontend

4. **Implement admin functions**

   * endpoint for banning a user for misbehave and getting a list of banned user
   * endpoint for unblock a user

5. **test relative to Comments/Users/Chat**

   improve test coverage

6. **improvement of overall reliability of backend code**



* Ziqi Yang

   1.modify chatspace function(#20)

    * add websocket API in front-end
    * make websocket API be connected with back-end
    * debug websocket function and make it work fluently

   2.add edit function to profile page(#19)

    * add edit button to profile page
    * write suitable API to update user's information of back-end

   3.add administrator function(#)

    * add a new identity "administrator"
    * add ban function, delete function to administrator, which means that administrator can ban someone or delete unsuitable topics and items.
    * administrator can release user from ban lists.

    4. modify ui(#16)



*  Zheyuan Fu

   1.modify hotitem fucntion in search page(#21)

    * modify API and make front-end can receive three hottest items from back-end
    * modify hotitem fucntion and make users can navigate to items by click them in hotitem.

   2.add sort function in createitem page (#17)

    * write a new select button and make user can choose "sorttype" by switch the button
    * add sort function and make items be sorted by the way in sorttype
    * write suitable API to get different sorted items from back-end.

   2.modify ui(#16)

    * write a new ui for login page
    * write a new ui for register page
    * write a new ui for lobby page
