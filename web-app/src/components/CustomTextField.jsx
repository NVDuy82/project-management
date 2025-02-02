import React from "react";
import { TextField } from "@mui/material";

const CustomTextField = ({ label, name, value, onChange, ...props }) => {
  return (
    <TextField
      label={label}
      name={name}
      value={value}
      onChange={onChange}
      fullWidth
      sx={{
        "& .MuiInputBase-input": {
          fontFamily: "'Google Sans', sans-serif",
          fontSize: "1rem", // Điều chỉnh kích thước chữ
          color: "#333", // Màu chữ
        },
        "& .MuiInputLabel-root": {
          fontFamily: "'Google Sans', sans-serif",
          fontSize: "0.9rem",
          color: "#555",
        },
        "& .MuiOutlinedInput-root": {
          "& fieldset": {
            borderColor: "#ccc", // Màu viền mặc định
          },
          "&:hover fieldset": {
            borderColor: "#000", // Màu viền khi hover
          },
          "&.Mui-focused fieldset": {
            borderColor: "#1976d2", // Màu viền khi focus
          },
        },
      }}
      {...props}
    />
  );
};

export default CustomTextField;
