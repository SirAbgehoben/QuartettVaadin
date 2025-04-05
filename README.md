# QuartettVaadin

## Overview

QuartettVaadin is a multiplayer card game built using Java, Vaadin, and Spring Boot. The project features visually appealing animations, a particle background effect, and a themed UI with custom CSS for an engaging user experience.

## Features

- **Multiplayer Gameplay**  
  Players join a queue and are paired into games using sessions. The game logic handles sessions, card distribution, and game actions.

- **Custom Themed UI**  
  The application uses Vaadin themes and custom CSS styles.

- **Dynamic Cards**  
  Cards are dynamically created from a shuffled deck. Attributes such as top speed, acceleration, power, and consumption are maintained for each card.

- **Particle Background**  
  Using Particle.js.

## Technologies Used

- Java (23+)
- Vaadin Flow (for UI components)
- Spring Boot (for backend services)
- CSS/JS (for animations and theme styling)
- Maven or Gradle (build tools)

## Project Structure

- `src/main/java/org/abgehoben/QuartettVaadin/`  
  Contains the source code for the game logic, including session management (`LoginService`), card handling (`Cards` and `Card`), and UI configuration.

- `src/main/frontend/`  
  Contains frontend assets such as CSS files and JavaScript modules.
    - `themes/my-theme/styles.css` – Custom CSS styling for the application.
    - `particles.js` – JavaScript file to create the particle background effect.

- `src/main/resources/META-INF/resources/frontend/themes/`  
  Contains additional CSS themes used for UI components.

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SirAbgehoben/QuartettVaadin.git
   ```

2. **Build the project**  
   Use Maven or Gradle to build the project. For example, with Maven:
   ```bash
   mvn clean install
   ```

3. **Run the application**  
   After building, run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**  
   Open your web browser and navigate to `http://localhost:8080`.

## Usage

Once the application is running, users can join the queue. When at least two players are connected, the game starts by pairing the sessions and dealing out randomized decks of cards. The application uses session management and dynamic UI updates via Vaadin push features.

## Customization

- **Themes and Styles:**  
  Update CSS files in `src/main/frontend/themes/my-theme/` or `src/main/resources/META-INF/resources/frontend/themes/` to modify the look and feel of the application.

- **Particle Effect:**  
  Modify `src/main/frontend/particles.js` to adjust the particle background behavior.

- **Game Logic:**  
  The card game logic is maintained in classes like `QuartettSession`, `Cards`, and `Card`. Adjust these classes to introduce new game features or change rules.

## Contributing

Contributions are welcome. Feel free to fork the repository and submit pull requests.

## License

This project is licensed under the  Unlicense license.