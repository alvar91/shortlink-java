# URL Shortening Service

## Description

This service provides functionality for shortening long URLs into unique short links. The application implements the following features:

- Accepts long URLs and generates unique short links.
- Limits the number of accesses to short links. The link is deleted once the access limit is reached.
- Deletes old or expired links after their lifetime has passed.
- Notifies users if a link becomes unavailable.
- Managed through the console, allowing users to access shortened links.

## Features

- URL shortening with unique identifiers.
- Support for a click limit on each link.
- Removal of expired links based on a specified lifetime.
- User notification when a link becomes unavailable.
- Simple command-line management.
- Configuration file for adjusting operational parameters.

## Installation

### 1. Clone the repository:

### 2. Build the project in IntelliJ IDEA or using Maven:

- **Using IntelliJ IDEA**:  
  Open the project in IntelliJ IDEA, and it should automatically detect and configure the project structure. Then, click on the "Build" menu and choose "Build Project."

- **Using Maven**:  
  Navigate to the project folder in the terminal and run the following command to build the project:

  ```bash
  mvn clean install
  ```

This will download all the necessary dependencies and compile the project.

### 3. Commands Supported by the Service

- `exit`: exit the program
- `register`: create a new user
- `login UUID`: login with an existing user
- `short url clicksLimit lifetimeHours`: shorten a link
- `open shortUrl`: open a shortened link
- `edit_clicks_limit shortUrl newLimit`: change the redirect limit
- `remove shortUrl`: remove a link
- `clear`: remove expired links

### 4. How to Test

1. Generate a user ID
2. Create a shortened link
3. Open the shortened link
4. Delete the shortened link
5. Modify the parameters of the shortened link
6. Switch users
7. Exit the program

After generating the UUID, it can be used for the operations with links described above. 
The UUID is tied to a specific link.
