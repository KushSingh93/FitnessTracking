package in.ongrid.fitnesstracker.service;

import in.ongrid.fitnesstracker.dao.UsersDao;
import in.ongrid.fitnesstracker.dto.LoginRequest;
import in.ongrid.fitnesstracker.dto.SignupRequest;
import in.ongrid.fitnesstracker.model.entities.User;
import in.ongrid.fitnesstracker.model.enums.Gender;
import in.ongrid.fitnesstracker.model.enums.UserType;
import in.ongrid.fitnesstracker.utils.JwtUtil;  // Assuming you have JWT Utility
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UsersDao usersDao;

    private JwtUtil jwtUtil;

    public AuthService(UsersDao usersDao, JwtUtil jwtUtil) {
        this.usersDao = usersDao;
        this.jwtUtil = jwtUtil;
    }

    // User Signup Method
    public String signup(SignupRequest request) {
        // Check if user already exists
        Optional<User> existingUser = usersDao.getUserByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if(request.getGender() == null) {
            user.setGender(Gender.MALE);
        }
        else{
            user.setGender(request.getGender());
        }
        String hashedPassword = BCrypt.hashpw(request.getPassword() , BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setUserType(UserType.USER);// Save to DB
        String email = request.getEmail();
        usersDao.saveUser(user);
        return jwtUtil.generateToken(email);
    }

    public String login(LoginRequest request) {
        // Fetch user by email
        Optional<User> userOptional = usersDao.getUserByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        User user = userOptional.get();

        // Validate password
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // Generate JWT token
        return jwtUtil.generateToken(user.getEmail());
    }
}
