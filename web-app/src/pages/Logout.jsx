import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {Box, CircularProgress, Typography} from "@mui/material";
import {logOut} from "../services/authenticationService";

export default function Logout() {
  const navigate = useNavigate();

  useEffect(() => {
    logOut();
    navigate("/login");
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
