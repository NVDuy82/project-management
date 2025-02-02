import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Box, CircularProgress, Typography} from "@mui/material";
import {changePassword, updateMyProfile, updateUser, useLoadUserInfo} from "../services/userService";
import Scene from "./Scene";
import InfoLine from "../components/InfoLine";
import {getMyProfile} from "../services/localStorageService";
import LogoutIcon from "@mui/icons-material/Logout";
import IconButton from "@mui/material/IconButton";
import TitleTypography from "../components/TitleTypography";
import {useSnackbar} from "../context/SnackbarContext";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import AssignTaskDialog from "../components/AssignTaskDialog";
import ChangePasswordDialog from "../components/ChangePasswordDialog";

export default function MyProfile() {

  const [isChangePasswordDialogOpen, setIsChangePasswordDialogOpen] = useState(false);
  const handleChangePassword = (data) => {
    changePassword(data)
      .then((response) => {
        showSnackbar("Đã đổi mật khẩu", "success");
        setIsChangePasswordDialogOpen(false);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const loadInfo = useLoadUserInfo();

  const [profile, setProfile] = useState(getMyProfile());

  useEffect(() => {
    loadInfo();
  }, [loadInfo, profile]);

  const updateFullName = async (newValue) => {
    try {
      const response = await updateMyProfile({ fullName: newValue });
      setProfile(response.data.result);
      showSnackbar("Cập nhật thành công", "success");

      return { success: true };
    } catch (error) {
      showSnackbar(error?.response?.data?.message, "error");

      return { success: false, error: error?.response?.data?.message };
    }
  }

  return (
    <Scene>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>
        <TitleTypography>
          Profile
        </TitleTypography>
      </Box>

      {profile ? (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "flex-start",
            width: "50%",
            gap: "10px",
          }}
        >
          {/*<Typography*/}
          {/*  sx={{*/}
          {/*    fontSize: 18,*/}
          {/*    mb: "40px",*/}
          {/*    fontFamily: "'Google Sans', sans-serif",*/}
          {/*  }}*/}
          {/*>*/}
          {/*  Hello, {profile.username} !*/}
          {/*</Typography>*/}

          <InfoLine
            label="Họ tên"
            value={profile.fullName}
            allowEdit={true}
            onSubmit={updateFullName}
          />
          <InfoLine label="User Id" value={profile.id} allowEdit={false} />
          <InfoLine label="Username" value={profile.username} allowEdit={false} />
          <InfoLine label="Email" value={profile.email} allowEdit={false} />
        </Box>
      ) : (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: "30px",
            justifyContent: "center",
            alignItems: "center",
            height: "50vh",
          }}
        >
          <CircularProgress></CircularProgress>
          <Typography>Loading ...</Typography>
        </Box>
      )}

      <Box sx={{ display: "flex", flexDirection: "column", alignItems: "flex-start" }}>
        <IconButton
          onClick={() => setIsChangePasswordDialogOpen(true)}
          size="small"
          sx={{
            marginTop: "20px", // Khoảng cách phía trên nút đăng xuất
            backgroundColor: "#e0e0e0", // Màu nền xám nhạt
            border: "1px solid #bdbdbd", // Viền xám nhạt
            borderRadius: "10px", // Bo góc
            '&:hover': {
              backgroundColor: "#bdbdbd", // Màu nền khi hover
            },
            padding: "10px", // Kích thước nút nhấn
          }}
        >
          <VpnKeyIcon style={{ color: "#424242" }} /> {/* Icon đổi mật khẩu */}
          <span
            style={{
              marginLeft: "6px", // Khoảng cách giữa icon và chữ
              fontSize: "15px", // Kích thước font
              fontFamily: "'Google Sans', sans-serif", // Font chữ
              color: "#424242", // Màu chữ xám đậm
              fontWeight: "bold", // Tô đậm chữ
            }}
          >
            Đổi mật khẩu
          </span>
        </IconButton>

        <IconButton
          onClick={() => navigate("/logout")}
          size="small"
          color="error"
          sx={{
            marginTop: "20px",
            backgroundColor: "#f8d7da",
            border: "1px solid #f5c6cb",
            borderRadius: "10px",
            '&:hover': {
              backgroundColor: "#f5c6cb",
            },
            padding: "10px",
          }}
        >
          <LogoutIcon />
          <span
            style={{
              marginLeft: "6px",
              fontSize: "15px",
              fontFamily: "'Google Sans', sans-serif",
              color: "#d33f3f",
              fontWeight: "bold",
            }}
          >
            Đăng xuất
          </span>
        </IconButton>
      </Box>

      <ChangePasswordDialog
        open={isChangePasswordDialogOpen}
        onClose={() => setIsChangePasswordDialogOpen(false)}
        onSubmit={handleChangePassword}
      />
    </Scene>
  );
}
