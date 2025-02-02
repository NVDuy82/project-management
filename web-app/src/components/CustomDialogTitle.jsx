import React from "react";
import { DialogTitle as MuiDialogTitle, Typography } from "@mui/material";

const CustomDialogTitle = ({ children, ...props }) => {
  return (
    <MuiDialogTitle
      {...props}
      sx={{
        fontWeight: "bold",
        textAlign: "center",
        fontSize: "1.65rem",
        fontFamily: "'Google Sans', sans-serif",
        ...props.sx, // Cho phép ghi đè thêm style nếu cần
      }}
    >
      <Typography component="h2" variant="h6" sx={{ fontSize: "inherit", fontWeight: "bold", }}>
        {children}
      </Typography>
    </MuiDialogTitle>
  );
};

export default CustomDialogTitle;
