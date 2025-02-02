import * as React from "react";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import { FaBell, FaProjectDiagram, FaTasks } from "react-icons/fa";
import {useLocation, useNavigate} from "react-router-dom"; // Import useLocation từ react-router-dom

// Styled components
const StyledListItem = styled(ListItem)(({ theme }) => ({
  padding: "0px",
}));

const MenuButton = styled(Button)(({ theme, selected }) => ({
  justifyContent: "flex-start",
  padding: "10px 24px",
  width: "100%",
  textTransform: "none",
  fontSize: "15px",
  fontFamily: "'Google Sans', sans-serif",
  color: selected ? "#005488" : "#000",
  backgroundColor: selected ? "#C2E7FF" : "transparent",
  fontWeight: 450,
  borderRadius: "100px",

  "&:hover": {
    backgroundColor: selected ? "#69c3ff" : "#E7E8EB",
  },

  "& .MuiButton-startIcon": {
    color: selected ? "#005488" : "#000",
  },
}));

function SideMenu() {
  const location = useLocation(); // Lấy thông tin đường dẫn hiện tại
  const navigate = useNavigate(); // Hook để điều hướng

  return (
    <List>
      <StyledListItem>
        <MenuButton
          selected={location.pathname === "/my-project"} // So sánh đường dẫn
          onClick={() => navigate("/my-project")} // Điều hướng đến /my-project
          startIcon={<FaProjectDiagram size={15} />}
        >
          Dự án của tôi
        </MenuButton>
      </StyledListItem>
      <StyledListItem>
        <MenuButton
          selected={location.pathname === "/my-task"} // So sánh đường dẫn
          onClick={() => navigate("/my-task")} // Điều hướng đến /my-tasks
          startIcon={<FaTasks size={15} />}
        >
          Công việc của tôi
        </MenuButton>
      </StyledListItem>
      <StyledListItem>
        <MenuButton
          selected={location.pathname === "/notifications"} // So sánh đường dẫn
          onClick={() => navigate("/notifications")} // Điều hướng đến /notifications
          startIcon={<FaBell size={15} />}
        >
          Thông báo
        </MenuButton>
      </StyledListItem>
    </List>
  );
}

export default SideMenu;
