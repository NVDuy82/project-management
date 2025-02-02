import React, {useState} from "react";
import {Box, Button, CircularProgress, Container, Grid, IconButton, InputAdornment, Typography,} from "@mui/material";
import {styled} from "@mui/system";
import {FaEye, FaEyeSlash} from "react-icons/fa";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import {useNavigate} from "react-router-dom";
import CustomTextField from "../components/CustomTextField";
import {createUser} from "../services/userService";

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
  backgroundColor: "#4CAF50", // Màu xanh lá
  color: "#fff", // Màu chữ trắng
  transition: "all 0.3s ease",
  "&:hover": {
    backgroundColor: "#45a049", // Màu khi hover, sẽ sáng hơn chút
    transform: "translateY(-2px)",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
  },
}));

const Register = () => {
  const navigate = useNavigate();
  const [fullName, setFullName] = useState(""); // State cho fullName
  let [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({ fullName: "", username: "", email: "", password: "", general: "" });

  const handleRegister = async (e) => {
    e.preventDefault();

    let newErrors = { fullName: "", username: "", email: "", password: "", general: "" };

    if (fullName.trim() === "") {
      newErrors.fullName = "Họ tên không được để trống";
    }
    if (username.trim() === "") {
      newErrors.username = "Username không được để trống";
    }
    if (email.trim() === "") {
      newErrors.email = "Email không được để trống";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      newErrors.email = "Email không hợp lệ";
    }
    if (password.trim() === "") {
      newErrors.password = "Mật khẩu không được để trống";
    }
    setErrors(newErrors);

    if (!newErrors.username && !newErrors.email && !newErrors.password && !newErrors.fullName) {
      setLoading(true);
      try {
        await createUser(
          username,
          password,
          email,
          fullName
        );
        navigate("/login");
      } catch (error) {
        newErrors.general = error.response?.data?.message || "Có lỗi xảy ra, vui lòng thử lại sau.";
        setErrors(newErrors);
      }
      setLoading(false);
    }
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
            Đăng ký
          </Typography>
          <form onSubmit={handleRegister} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <CustomTextField
                  fullWidth
                  label="Họ Tên"
                  variant="outlined"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  error={!!errors.fullName}
                  helperText={errors.fullName}
                  required
                  inputProps={{
                    "aria-label": "Full Name",
                  }}
                />
              </Grid>
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
                  label="Email"
                  variant="outlined"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  error={!!errors.email}
                  helperText={errors.email}
                  required
                  inputProps={{
                    "aria-label": "Email",
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
                  autoComplete="new-password"
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
                  sx={{ paddingTop: 1 }}
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
              aria-label="Register"
            >
              {loading ? <CircularProgress size={24} color="inherit" /> : "Đăng ký"}
            </StyledButton>
          </form>
        </FormContainer>
      </BackgroundContainer>
    </ThemeProvider>
  );
};

export default Register;
