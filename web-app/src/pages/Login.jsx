import React, {useState} from "react";
import {Box, Button, CircularProgress, Container, Grid, IconButton, InputAdornment, Typography,} from "@mui/material";
import {styled} from "@mui/system";
import {FaEye, FaEyeSlash} from "react-icons/fa";
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {logIn} from "../services/authenticationService";
import {useNavigate} from "react-router-dom";
import CustomTextField from "../components/CustomTextField";
import {getMyInfo} from "../services/userService";

const theme = createTheme();

const BackgroundContainer = styled(Box)(({}) => ({
  backgroundImage: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
  minHeight: "100vh",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
}));

const FormContainer = styled(Container)(({ theme }) => ({
  backgroundColor: "#ffffff",
  borderRadius: theme.spacing(2),
  padding: theme.spacing(4),
  boxShadow: "0 10px 25px rgba(0, 0, 0, 0.1)",
  transition: "all 0.3s ease-in-out",
  "&:hover": {
    boxShadow: "0 15px 30px rgba(0, 0, 0, 0.15)",
  },
}));

const StyledButton = styled(Button)(({ theme }) => ({
  marginTop: theme.spacing(2),
  padding: theme.spacing(1.5),
  fontSize: "1rem",
  fontWeight: "bold",
  fontFamily: "'Google Sans', sans-serif",
  transition: "all 0.3s ease",
  "&:hover": {
    transform: "translateY(-2px)",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  },
}));

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({ username: "", password: "", general: "" });

  const handleLogin = async (e) => {
    e.preventDefault();

    let newErrors = {username: "", password: "", general: ""};
    if (username.trim() === "") {
      newErrors.username = "Username cannot be empty";
    }
    if (password.trim() === "") {
      newErrors.password = "Password cannot be empty";
    }
    setErrors(newErrors);

    if (!newErrors.username && !newErrors.password) {
      setLoading(true);
      try {
        await logIn(username, password);

        getMyInfo()
          .then((response) => {
            if (response.data.result?.roles[0]?.name === "USER") {
              navigate("/my-project");
            } else if (response.data.result?.roles[0]?.name === "ADMIN") {
              navigate("/admin");
            }
          })
          .catch((error) => {
            const errorResponse = error.response?.data;
            newErrors.general = errorResponse?.message || "Có lỗi xảy ra, vui lòng thử lại sau.";
            newErrors.general = newErrors.general === "Unauthenticated" ? "Mật khẩu không chính xác" : newErrors.general;
            setErrors(newErrors);
          })

      } catch (error) {
        const errorResponse = error.response?.data;
        newErrors.general = errorResponse?.message || "Có lỗi xảy ra, vui lòng thử lại sau.";
        newErrors.general = newErrors.general === "Unauthenticated" ? "Mật khẩu không chính xác" : newErrors.general;
        setErrors(newErrors);
      }
      setLoading(false);
    }
  };

  const handleNavigateToRegister = () => {
    navigate("/register");
  };

  return (
    <ThemeProvider theme={theme}>
      <BackgroundContainer>
        <FormContainer maxWidth="sm">
          <Typography
            variant="h4"
            component="h1"
            align="center"
            gutterBottom
            sx={{
              fontFamily: "'Google Sans', sans-serif",
              padding: 1,
            }}
          >
            Login
          </Typography>
          <form onSubmit={handleLogin} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <CustomTextField
                  fullWidth
                  label="Username"
                  variant="outlined"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  error={!!errors.username}
                  helperText={errors.username}
                  required
                  inputProps={{
                    "aria-label": "Username",
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <CustomTextField
                  fullWidth
                  label="Password"
                  variant="outlined"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  error={!!errors.password}
                  helperText={errors.password}
                  required
                  autoComplete="current-password"
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={() => setShowPassword(!showPassword)}
                          edge="end"
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  inputProps={{
                    "aria-label": "Password",
                  }}
                />
              </Grid>
            </Grid>
            <Grid item xs={12}>
              {errors.general && (
                <Typography
                  color="error"
                  variant="body2"
                  align="center"
                  gutterBottom
                  sx={{
                    paddingTop: 1,
                  }}
                >
                  {errors.general}
                </Typography>
              )}
            </Grid>
            <StyledButton
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              disabled={loading}
              aria-label="Login"
            >
              {loading ? <CircularProgress size={24} color="inherit" /> : "Login"}
            </StyledButton>
            <StyledButton
              fullWidth
              variant="outlined"
              color="secondary"
              onClick={handleNavigateToRegister}
              aria-label="Register"
            >
              Register
            </StyledButton>
          </form>
        </FormContainer>
      </BackgroundContainer>
    </ThemeProvider>
  );
};

export default Login;
