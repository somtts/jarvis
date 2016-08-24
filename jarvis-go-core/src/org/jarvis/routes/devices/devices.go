package devices

import native "org/jarvis/native"
import http "net/http"
import "encoding/json"
import "io/ioutil"
import "fmt"

import (
	log "github.com/Sirupsen/logrus"
	logger "org/jarvis/logger"
)

type DioResource struct {
	Pin        int  `json:"pin"`
	Sender     int  `json:"sender"`
	Interuptor int  `json:"interruptor"`
	On         bool `json:"on"`
}

func PostHandler(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
		/**
		 * decode the body
		 */
		body, err := ioutil.ReadAll(req.Body)
		if err != nil {
			http.Error(w, "{}", 400)
		}

		/**
		 * decode json
		 */
		var m DioResource
		err = json.Unmarshal(body, &m)
		if err != nil {
			http.Error(w, fmt.Sprintf("%#v", m), 400)
			next.ServeHTTP(w, req)
		} else {
			/**
			 * nominal case
			 */
			logger.NewLogger().WithFields(log.Fields{
				"pin":         m.Pin,
				"sender":      m.Sender,
				"interruptor": m.Interuptor,
				"on":          m.On,
			}).Info("DIO")

			if m.On {
				native.DioOn(m.Pin, m.Sender, m.Interuptor)
			} else {
				native.DioOff(m.Pin, m.Sender, m.Interuptor)
			}
			w.Header().Set("Content-Type", "application/json")
			w.Write([]byte(body))
			next.ServeHTTP(w, req)
		}
	})
}
