import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {Box, CircularProgress, Typography} from "@mui/material";
import {isAuthenticated} from "../services/authenticationService";

export default function Home() {
  const navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      navigate("/my-project");
    }
  }, [navigate]);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "30px",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
      }}
    >
      <CircularProgress></CircularProgress>
      <Typography>Loading ...</Typography>
    </Box>
  );
}
