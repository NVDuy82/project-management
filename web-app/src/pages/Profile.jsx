import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, CircularProgress, Typography} from "@mui/material";
import {getProfile} from "../services/userService";
import Scene from "./Scene";
import InfoLine from "../components/InfoLine";
import TitleTypography from "../components/TitleTypography";
import {useSnackbar} from "../context/SnackbarContext";

export default function Profile() {
  const { userId } = useParams();
  const { showSnackbar } = useSnackbar();

  const navigate = useNavigate();

  const [profile, setProfile] = useState();

  useEffect(() => {
    getProfile(userId)
      .then((response) => {
        setProfile(response.data.result);
        console.log(response.data.result);
        console.log(profile);
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, []);

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
            allowEdit={false}
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
    </Scene>
  );
}
