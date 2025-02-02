import * as React from "react";
import { styled, MenuItem } from "@mui/material";

const Custom = styled(MenuItem)(({ theme }) => ({
  fontSize: 14,
  fontFamily: "'Google Sans', sans-serif",
}));

// Component nhận onClick và children từ props
function CustomMenuItem({ onClick, children }) {
  return (
    <Custom onClick={onClick}>
      {children}
    </Custom>
  );
}

export default CustomMenuItem;
