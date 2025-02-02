import * as React from "react";
import {useState} from "react";
import { useNavigate } from "react-router-dom";
import {styled} from "@mui/material/styles";
import Box from "@mui/material/Box";
import IconButton from "@mui/material/IconButton";
import {AppBar, InputAdornment, Link, TextField, Toolbar, Typography} from "@mui/material";
import {FaCog, FaInfoCircle, FaSearch} from "react-icons/fa";
import {getFromLocalStorage, getMyProfile} from "../services/localStorageService";

const StyledAppBar = styled(AppBar)(({ theme }) => ({
  backgroundColor: "#ffffff",
  color: "#000000",
  boxShadow: "0px 2px 4px rgba(0, 0, 0, 0.1)"
}));

const SearchTextField = styled(TextField)(({ theme }) => ({
  backgroundColor: "#E9EEF6",
  borderRadius: "50px",
  "& .MuiOutlinedInput-root": {
    "& fieldset": {
      borderColor: "transparent",
    },
    "&:hover fieldset": {
      borderColor: "transparent",
    },
    "&.Mui-focused fieldset": {
      borderColor: "transparent",
    },
    "& .MuiInputBase-input": {
      padding: "12px 14px",
    },
  },
}));

export default function Header() {
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();

  return (
    <StyledAppBar
      position="sticky"
      sx={{
        backgroundColor: "#F8FAFD",
        boxShadow: "none",
        borderBottom: "none",
        margin: 0,
        padding: "3px 0px",
      }}
    >
      <Toolbar>
        <Box
          sx={{ display: "flex", alignItems: "center", width: "100%" }}>
          <Box sx={{ display: "flex", alignItems: "center", mr: 4 }}>
            <img
              src="https://images.unsplash.com/photo-1633332755192-727a05c4013d?w=48&h=48&fit=crop"
              alt="User profile"
              style={{width: 48, height: 48, borderRadius: "50%", marginRight: 16, cursor: "pointer"}}
              onClick={() => navigate("/profile")}
            />
            <Typography
              sx={{
                fontFamily: "'Google Sans', sans-serif",
              }}
              variant="h6"
            >
              {getMyProfile()?.fullName}
            </Typography>
          </Box>

          <SearchTextField
            fullWidth
            placeholder="Search..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <FaSearch />
                </InputAdornment>
              ),
            }}
            sx={{ maxWidth: 400, mx: "auto" }}
          />

          <Box sx={{ ml: "auto" }}>
            <IconButton color="inherit" aria-label="info">
              <FaInfoCircle size={20} />
            </IconButton>
            <IconButton color="inherit" aria-label="settings">
              <FaCog size={20} />
            </IconButton>
          </Box>
        </Box>
      </Toolbar>
    </StyledAppBar>
  );
}
