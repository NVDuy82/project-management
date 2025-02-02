import * as React from "react";
import Box from "@mui/material/Box";
import {Container, Grid, Paper} from "@mui/material";
import Header from "../components/Header";
import SideMenu from "../components/SideMenu";

function Scene({ children }) {

  return (
    <Box sx={{ flexGrow: 1, backgroundColor: "#F8FAFD" }}>
      <Header />

      <Container
        maxWidth="100%"
        sx={{
          mt: 3,
          height: "calc(100vh - 90px)",
          display: "flex",
          flexDirection: "column",
          margin: "6px 0px",
          padding: "0px",
        }}
      >
        <Grid container spacing={3}>
          <Grid item xs={12} md={2.5}>
            <Paper
              elevation={0}
              sx={{ p: 2, backgroundColor: "transparent", padding: "6px 0px" }}
            >
              <SideMenu />
            </Paper>
          </Grid>

          <Grid item xs={12} md={9.5}>
            <Paper elevation={0} sx={{ p: 3, borderRadius: "20px" }}>
              { children }
            </Paper>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}

export default Scene;
